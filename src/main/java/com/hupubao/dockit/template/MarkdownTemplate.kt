package com.hupubao.dockit.template

import com.hupubao.dockit.entity.ClassNode
import com.hupubao.dockit.entity.MethodCommentNode

class MarkdownTemplate : Template {


    constructor() : super()
    constructor(source: String, methodCommentNode: MethodCommentNode) : super(source, methodCommentNode)


    fun toHtml() {

    }

}