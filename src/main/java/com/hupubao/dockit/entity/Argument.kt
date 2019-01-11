package com.hupubao.dockit.entity

import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.constants.TemplatePlaceholder

class Argument {

    @Placeholder(TemplatePlaceholder.ARGUMENT_NAME_ORIGIN)
    var originName: String = ""
    @Placeholder(TemplatePlaceholder.ARGUMENT_NAME)
    var name: String = ""
    @Placeholder(TemplatePlaceholder.ARGUMENT_DESCRIPTION)
    var description: String? = null
    @Placeholder(TemplatePlaceholder.ARGUMENT_REQUIRED)
    var required: String? = "Yes"
    @Placeholder(TemplatePlaceholder.ARGUMENT_TYPE)
    var type: String = "String"
    val children: MutableList<Argument> = mutableListOf()

    var level: Int = 0
    var levelPrefix: String = ""

    constructor(originName: String, name: String, description: String?, required: String?, type: String) {
        this.originName = originName
        this.name = name
        this.description = description
        this.required = required
        this.type = type
    }

}
