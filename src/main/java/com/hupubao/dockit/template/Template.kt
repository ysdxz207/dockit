package com.hupubao.dockit.template

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.constants.TemplatePlaceholder
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.PlaceholderType
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.hupubao.dockit.utils.ProjectUtils
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

open class Template(project: MavenProject, log: Log, var source: String, methodCommentNode: MethodCommentNode) {
    lateinit var document: Node

    @Placeholder(TemplatePlaceholder.title)
    var title: String = ""
    @Placeholder(TemplatePlaceholder.desc, type = PlaceholderType.LIST)
    var descriptionList: MutableList<String> = mutableListOf()
    @Placeholder(TemplatePlaceholder.url)
    var requestUrl: String = ""
    @Placeholder(TemplatePlaceholder.method)
    var requestMethod: String = ""
    @Placeholder(TemplatePlaceholder.arg, type = PlaceholderType.LIST)
    var argList: MutableList<Argument> = mutableListOf()
    @Placeholder(TemplatePlaceholder.resArg, type = PlaceholderType.LIST)
    var resArgList: MutableList<Argument> = mutableListOf()
    @Placeholder(TemplatePlaceholder.resSample)
    var resSample: String = ""
    @Placeholder(TemplatePlaceholder.remark)
    var remark: String? = null


    var responseObjectClassName: String? = null
    private var project: MavenProject? = project
    private var log: Log? = log

    init {
        this.title = if (methodCommentNode.title == null) methodCommentNode.methodName!! else methodCommentNode.title!!
        this.descriptionList = methodCommentNode.descriptionList
        this.requestUrl = methodCommentNode.requestUrl
        this.requestMethod = methodCommentNode.requestMethod
        this.argList = methodCommentNode.requestArgList
        this.resArgList = methodCommentNode.responseArgList
        this.responseObjectClassName = methodCommentNode.responseObjectClassName
        this.parse()
    }


    private fun parse() {
        document = Parser.builder().build().parse(source)!!
        resSample = mockResponseData()
        println("mock data:$resSample")
        for (node in document.children) {
            val matchResult = ("""\$\{\w+\.*\w+\}""".toRegex()).findAll(node.chars.toString())
            if (matchResult.none()) {
                continue
            }

            if (matchResult.count() == 1) {
                PlaceholderResolver.resolve(node, mutableListOf(matchResult.single().value), this)
            } else {
                val arr = mutableListOf<String>()
                matchResult.forEach { p ->
                    arr.add(p.value)
                }
                PlaceholderResolver.resolve(node, arr, this)
            }
        }
    }

    private fun mockResponseData(): String {
        var result = ""
        if (responseObjectClassName != null) {

            var subClassName = ""
            if (responseObjectClassName!!.contains("<")) {
                subClassName = responseObjectClassName!!.substring(responseObjectClassName!!.indexOf("<") + 1, responseObjectClassName!!.indexOf(">"))
            }

            val clazzOptional = ProjectUtils.loadClass(project!!, log!!, subClassName)
            clazzOptional.ifPresent { clazz ->
                result = mockData(clazz)
            }
        }

        return result
    }

    private fun mockData(resArgList: List<Argument>): String {
        val data = JSONObject()
        resArgList.forEach { argument ->
            val typeOptional = ProjectUtils.loadClass(project!!, log!!, argument.type!!)

            var value: Any = JMockData.mock(String::class.java)
            typeOptional.ifPresent { type ->
                value = JMockData.mock(type)
            }
            data[argument.name] = value
        }
        return JSON.toJSONString(data, SerializerFeature.PrettyFormat)
    }

    /**
     * todo
     */
    private fun mockData(clazz: Class<*>): String {
        val data = if (clazz.newInstance() is Iterable<*>) {
            mutableListOf(clazz)
        } else {
            JMockData.mock(clazz)
        }
        return JSON.toJSONString(data, SerializerFeature.PrettyFormat)
    }

    fun render(): String {

        val sb = StringBuilder()
        for (node in document.children) {
            sb.append(node.chars).append(BasedSequence.EOL_CHARS)
        }

        return sb.toString()
    }
}