package com.hupubao.dockit.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.enums.HttpMethod
import com.hupubao.dockit.resolver.template.DescriptionResolver
import com.hupubao.dockit.resolver.template.TitleResolver
import org.commonmark.node.Node
import org.commonmark.parser.Parser

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


    constructor(source: String) {
        this.source = source
        this.resolve()
    }

    fun resolve() {
        document = Parser.builder().build().parse(source)
    }
    fun render(): String {
        TitleResolver.resolve(document, title)
        DescriptionResolver.resolve(document, descriptionList)

    }
}