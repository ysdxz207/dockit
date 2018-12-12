package com.hupubao.dockit.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.HttpMethod
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import kotlin.reflect.full.memberProperties

open class Template {
    var source: String = ""
    lateinit var document: Node

    var title: String = ""
    var descriptionList: MutableList<String> = mutableListOf()
    var requestUrl: String = ""
    var requestMethod: String = ""
    var argList: MutableList<Argument> = mutableListOf()
    var resArgList: MutableList<Argument> = mutableListOf()
    var resSample: String = ""
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
    }
    fun render(): String {
        this::class.memberProperties.forEach { field ->
            var property = field.name
            val value = field.getter.call(this) ?: return@forEach
            if (value is Iterable<*>) {
                property = property.replace("List", "")
            }
            PlaceholderResolver.resolve(document, property, value)
        }

        val sb = StringBuilder()
        document.children.forEach {
            sb.append(it.chars).append("\r\n")
        }

        return sb.toString()
    }
}