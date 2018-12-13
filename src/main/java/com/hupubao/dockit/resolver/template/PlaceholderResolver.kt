package com.hupubao.dockit.resolver.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.template.MarkdownTemplate
import java.nio.charset.Charset


object PlaceholderResolver : BasePlaceholderResolver(){



    @JvmStatic
    fun main(args: Array<String>) {
        val text = javaClass.getResource("/template/DEFAULT.MD").readText(Charset.forName("UTF-8"))

        val methodCommentNode = MethodCommentNode()
        methodCommentNode.title = "大表哥"
        methodCommentNode.descriptionList.add("描述1")
        methodCommentNode.descriptionList.add("描述2")
        methodCommentNode.descriptionList.add("描述3")
        methodCommentNode.descriptionList.add("描述4")
        methodCommentNode.requestUrl = "/api"
        methodCommentNode.requestMethod = "GET,POST"
        methodCommentNode.requestArgList.add(Argument("page", "页码", true, "Integer"))
        println(MarkdownTemplate(text, methodCommentNode).render())
    }
}