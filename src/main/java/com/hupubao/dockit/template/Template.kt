package com.hupubao.dockit.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.enums.HttpMethod
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser

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


    constructor(source: String) {
        this.source = source
        this.resolve()
    }


    fun resolve() {
        document = Parser.builder().build().parse(source)
    }
    fun render(): String {
        PlaceholderResolver.resolve(document, "", title)
        return document.toString()
    }
}