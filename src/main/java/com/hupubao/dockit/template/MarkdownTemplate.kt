package com.hupubao.dockit.template

import com.hupubao.dockit.entity.ClassNode

class MarkdownTemplate : Template {


    constructor() : super()
    constructor(source: String, classNode: ClassNode) : super(source, classNode)


    fun toHtml() {

    }

}