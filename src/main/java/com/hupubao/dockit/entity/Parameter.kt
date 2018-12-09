package com.hupubao.dockit.entity

class Parameter {

    var paramName: String? = null
    var paramDescription: String? = null
    var required: Boolean? = null
    var type: String? = null

    constructor(paramName: String?, paramDescription: String?, required: Boolean?, type: String?) {
        this.paramName = paramName
        this.paramDescription = paramDescription
        this.required = required
        this.type = type
    }
}
