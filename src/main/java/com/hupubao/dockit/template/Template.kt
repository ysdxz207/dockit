package com.hupubao.dockit.template

import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.PlaceholderType
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.hupubao.dockit.resolver.template.TestPlaceholderResolver
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.sequence.BasedSequence

open class Template {
    var source: String = ""
    lateinit var document: Node

    @Placeholder("title")
    var title: String = ""
    @Placeholder("description", type = PlaceholderType.LIST)
    var descriptionList: MutableList<String> = mutableListOf()
    @Placeholder("requestUrl")
    var requestUrl: String = ""
    @Placeholder("requestMethod")
    var requestMethod: String = ""
    @Placeholder("arg", type = PlaceholderType.LIST)
    var argList: MutableList<Argument> = mutableListOf()
    @Placeholder("resArg", type = PlaceholderType.LIST)
    var resArgList: MutableList<Argument> = mutableListOf()
    @Placeholder("resSample")
    var resSample: String = ""
    @Placeholder("remark")
    var remark: String? = null

    constructor()


    constructor(source: String, methodCommentNode: MethodCommentNode) {
        this.source = source
        this.title = if (methodCommentNode.title == null) methodCommentNode.methodName!! else methodCommentNode.title!!
        this.descriptionList = methodCommentNode.descriptionList
        this.requestUrl = methodCommentNode.requestUrl
        this.requestMethod = methodCommentNode.requestMethod
        this.argList = methodCommentNode.requestArgList
        this.resArgList = methodCommentNode.responseArgList

        this.parse()
    }


    private fun parse() {
        document = Parser.builder().build().parse(source)
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
    fun render(): String {

        val sb = StringBuilder()
        for (node in document.children) {
            sb.append(node.chars).append(BasedSequence.EOL_CHARS)
        }

        return sb.toString()
    }
}