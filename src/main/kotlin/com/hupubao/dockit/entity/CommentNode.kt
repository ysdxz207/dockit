package com.hupubao.dockit.entity

class CommentNode {

    var description: String? = null
    var requestUrl: String? = null
    var requestMethod: String? = null
    var requestParameterList: MutableList<Parameter> = mutableListOf()
    var responseParameterList: MutableList<Parameter> = mutableListOf()
}
