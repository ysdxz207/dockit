package com.hupubao.dockit.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.ClassNode
import com.hupubao.dockit.enums.HttpMethod
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import java.lang.StringBuilder
import kotlin.reflect.full.memberProperties

open class Template {
    var source: String = ""
    lateinit var document: Node

    var title: String = ""
    var descriptionList: MutableList<String> = mutableListOf()
    var requestUrl: String = ""
    var requestMethod: Array<HttpMethod> = emptyArray()
    var argList: MutableList<Argument> = mutableListOf()
    var resArgList: MutableList<Argument> = mutableListOf()
    var resSample: String = ""
    var remark: String? = null

    constructor()


    constructor(source: String, classNode: ClassNode) {
        this.source = source
        this.title = if (classNode.classDescription == null) classNode.className!! else classNode.classDescription!!

        this.parse()
    }


    private fun parse() {
        document = Parser.builder().build().parse(source)
    }
    fun render(): String {
        this::class.memberProperties.forEach { field ->
            PlaceholderResolver.resolve(document, field.name, field.getter.call(this))
        }

        val sb = StringBuilder()
        document.children.forEach {
            sb.append(it.chars).append("\r\n")
        }

        println(document.chars)
        return sb.toString()
    }
}