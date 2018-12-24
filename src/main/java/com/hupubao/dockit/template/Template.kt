package com.hupubao.dockit.template

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.hupubao.dockit.utils.MockUtils
import com.hupubao.dockit.utils.ProjectUtils
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.lang.Exception
import java.util.*

open class Template(
    project: MavenProject, log: Log, var source: String,
    private var methodCommentNode: MethodCommentNode
) {
    lateinit var document: Node

    private var project: MavenProject? = project
    private var log: Log? = log

    init {
        this.parse()
    }


    private fun parse() {
        document = Parser.builder().build().parse(source)!!
        methodCommentNode.resSample = mockResponseData()
        for (node in document.children) {
            val matchResult = ("""\$\{\w+\.*\w+\}""".toRegex()).findAll(node.chars.toString())
            if (matchResult.none()) {
                continue
            }

            if (matchResult.count() == 1) {
                PlaceholderResolver.resolve(node, mutableListOf(matchResult.single().value), this.methodCommentNode)
            } else {
                val arr = mutableListOf<String>()
                matchResult.forEach { p ->
                    arr.add(p.value)
                }
                PlaceholderResolver.resolve(node, arr, this.methodCommentNode)
            }
        }
    }

    private fun mockResponseData(): String {
        var result = ""
        if (methodCommentNode.responseObjectClassName != null) {

            var subClassName = ""
            if (methodCommentNode.responseObjectClassName!!.contains("<")) {
                subClassName = methodCommentNode.responseObjectClassName!!.substring(
                    methodCommentNode.responseObjectClassName!!.indexOf("<") + 1,
                    methodCommentNode.responseObjectClassName!!.indexOf(">")
                )
            }

            val clazzOptional = ProjectUtils.loadClass(project!!, log!!, subClassName)
            clazzOptional.ifPresent { clazz ->
                result = mockJSONObjectData(clazz)
            }
        } else {
            result = mockData(methodCommentNode.responseArgList)
        }

        return result
    }

    private fun mockData(resArgList: List<Argument>): String {

        var json: Any = ""
            // array
            val arr = mockArrayData(resArgList)
        if (arr is JSONArray) {
            json = arr
        } else if (arr is JSONObject){
            json = JSONObject()
            val obj = mockObjectData(resArgList)
            json.putAll(obj)
            json.putAll(mockCommonData(resArgList))
            json.putAll(arr)
        }

        return JSON.toJSONString(json, SerializerFeature.PrettyFormat)

    }

    private fun mockArrayData(resArgList: List<Argument>): JSON {

        val json: JSON
        var listKeys = resArgList.groupBy { parseArrayArgNameKey(it) }

        if (listKeys.count() == 1) {
            // array
            val array = JSONArray()
            array.add(mockJSONObjectData(listKeys[listKeys.keys.last().toString()]!!))
            json = array
        } else {
            listKeys = resArgList.filter { parseArrayArgName(it) != it.name }.groupBy { parseArrayArgNameKey(it) }

            json = JSONObject()
            listKeys.forEach { t, u ->
                val array = JSONArray()
                array.add(mockJSONObjectData(u))
                json[t] = array
            }

        }

        return json
    }

    private fun mockObjectData(resArgList: List<Argument>): JSONObject {

        val json: JSON
        val listKeys = resArgList.filter { parseObjectArgName(it) != it.name }.groupBy { parseObjectArgNameKey(it) }

        json = JSONObject()
        listKeys.forEach { t, u ->
            json[t] = mockJSONObjectData(u)
        }

        return json
    }


    private fun mockCommonData(resArgList: List<Argument>): JSONObject {

        val list = resArgList.filter { parseObjectArgName(it) == it.name && parseArrayArgName(it) == it.name }
        return mockJSONObjectData(list)
    }

    private fun parseArrayArgNameKey(argument: Argument): String {
        return if (argument.name.contains("[")) {
            argument.name.substring(0, argument.name.indexOf("["))
        } else {
            argument.name
        }
    }

    private fun parseArrayArgName(argument: Argument): String {
        return if (argument.name.contains("[") && argument.name.contains("]")) {
            argument.name.substring(argument.name.indexOf("[") + 1, argument.name.indexOf("]"))
        } else {
            argument.name
        }
    }

    private fun parseObjectArgNameKey(argument: Argument): String {
        return if (argument.name.contains(".")) {
            argument.name.substring(0, argument.name.indexOf("."))
        } else {
            argument.name
        }
    }

    private fun parseObjectArgName(argument: Argument): String {
        return if (argument.name.contains(".")) {
            argument.name.substring(argument.name.indexOf(".") + 1)
        } else {
            argument.name
        }
    }

    private fun mockJSONObjectData(resArgList: List<Argument>): JSONObject {
        val data = JSONObject()
        resArgList.forEach { argument ->
            val typeOptional = getArgumentJavaType(argument)

            var value: Any = MockUtils.mockFromEnglishSeed()
            typeOptional.ifPresent { type ->
                value = JMockData.mock(type)
            }
            data[parseArgName(argument)] = value
        }
        return data
    }

    private fun parseArgName(argument: Argument): String {

        if (argument.name.contains("[") && argument.name.contains("]")) {
            return parseArrayArgName(argument)
        }

        if (argument.name.contains(".")) {
            return parseObjectArgName(argument)
        }

        return argument.name
    }

    private fun getArgumentJavaType(argument: Argument): Optional<Class<*>> {

        return try {
            Optional.of(Class.forName("java.lang.${argument.type}"))
        } catch (e: Exception) {
            ProjectUtils.loadClass(project!!, log!!, argument.name)
        }
    }

    private fun mockJSONObjectData(clazz: Class<*>): String {
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