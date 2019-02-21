package com.hupubao.dockit.entity

import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.constants.TemplatePlaceholder
import com.hupubao.dockit.enums.PlaceholderType

open class MethodCommentNode : Cloneable {

    var methodName: String? = null
    @Placeholder(TemplatePlaceholder.title)
    var title: String? = null
    @Placeholder(TemplatePlaceholder.status)
    var status: String? = null
    @Placeholder(TemplatePlaceholder.version)
    var version: String? = null
    @Placeholder(TemplatePlaceholder.desc, type = PlaceholderType.LIST)
    var descriptionList: MutableList<String> = mutableListOf()
    @Placeholder(TemplatePlaceholder.url)
    var requestUrl: String = ""
    @Placeholder(TemplatePlaceholder.method)
    var requestMethod: String = ""
    @Placeholder(TemplatePlaceholder.arg, type = PlaceholderType.LIST)
    var requestArgList: MutableList<Argument> = mutableListOf()
    @Placeholder(TemplatePlaceholder.resArg, type = PlaceholderType.LIST)
    var responseArgList: MutableList<Argument> = mutableListOf()
    var responseObjectClassName: String? = null
    @Placeholder(TemplatePlaceholder.reqSample)
    var reqSample: String = ""
    @Placeholder(TemplatePlaceholder.resSample)
    var resSample: String = ""
    @Placeholder(TemplatePlaceholder.remark)
    var remark: String? = ""


    public override fun clone(): MethodCommentNode {
        return super.clone() as MethodCommentNode
    }
}
