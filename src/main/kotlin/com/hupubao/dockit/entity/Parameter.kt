package com.hupubao.dockit.entity

class Parameter {

    var paramName: String? = null
    var paramDescription: String? = null

    constructor(paramName: String?, paramDescription: String?) {
        this.paramName = paramName
        this.paramDescription = paramDescription
    }
}
