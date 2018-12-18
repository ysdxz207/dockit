package com.hupubao.dockit.entity

import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.constants.TemplatePlaceholder

class Argument {

    @Placeholder(TemplatePlaceholder.argumentName)
    var name: String? = null
    @Placeholder(TemplatePlaceholder.argumentDescription)
    var description: String? = null
    @Placeholder(TemplatePlaceholder.argumentRequired)
    var required: String? = "Yes"
    @Placeholder(TemplatePlaceholder.argumentType)
    var type: String? = null

    constructor(name: String?, description: String?, required: String?, type: String?) {
        this.name = name
        this.description = description
        this.required = required
        this.type = type
    }
}
