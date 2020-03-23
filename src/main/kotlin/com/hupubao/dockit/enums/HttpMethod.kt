package com.hupubao.dockit.enums

enum class HttpMethod {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;


    fun getHttpMethod(methodName: String): HttpMethod {
        return valueOf(methodName)
    }
}