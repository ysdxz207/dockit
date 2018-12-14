package com.hupubao.dockit.entity

import com.hupubao.dockit.annotation.Placeholder

class Argument {

    @Placeholder("name")
    var name: String? = null
    @Placeholder("description")
    var description: String? = null
    @Placeholder("required")
    var required: String? = "Yes"
    @Placeholder("type")
    var type: String? = null

    constructor(name: String?, description: String?, required: String?, type: String?) {
        this.name = name
        this.description = description
        this.required = required
        this.type = type
    }
}
