package com.hupubao.dockit.entity

class ClassNode {
    var className: String = ""
    var classDescription: String? = null
    var methodCommentNodeList: MutableList<MethodCommentNode> = mutableListOf()


    fun classNameOrClassDescription(): String? {
        return if (classDescription == null) className else classDescription
    }
}
