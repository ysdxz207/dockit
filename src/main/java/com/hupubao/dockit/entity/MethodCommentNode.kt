package com.hupubao.dockit.entity

class MethodCommentNode {


    var methodName: String? = null
    var title: String? = null
    var descriptionList: MutableList<String> = mutableListOf()
    var requestUrl: String? = null
    var requestMethod: String? = null
    var requestArgList: MutableList<Argument> = mutableListOf()
    var responseArgList: MutableList<Argument> = mutableListOf()
    var responseObjectClassName: String? = null


}
