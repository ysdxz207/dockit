package com.hupubao.dockit.resolver.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.template.MarkdownTemplate
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.Node
import java.nio.charset.Charset
import kotlin.reflect.full.memberProperties


object PlaceholderResolver {

    fun isSimpleType(clazz: Class<*>): Boolean {
        return when (clazz) {
            Integer::class.java,
            Short::class.java,
            Long::class.java,
            Double::class.java,
            Float::class.java,
            Byte::class.java,
            Char::class.java,
            String::class.java,
            Boolean::class.java -> true
            else -> {
                false
            }
        }
    }

    fun resolve(document: Node, property: String, value: Any) {

        val placeholder = "\${$property}"
        document.children.map { node ->
            if (node.chars.contains(placeholder)) {
                if (isSimpleType(value::class.java)) {
                    if (node != null) {
                        var charsWithValue = node.chars.replace(placeholder, value.toString())
                        charsWithValue =
                                if (charsWithValue.endsWith("\n") || charsWithValue.endsWith("\r\n")) charsWithValue else charsWithValue.append(
                                    "\r\n"
                                )
                        node.chars = charsWithValue
                    }
                } else if (value is Iterable<*>) {
                    // list
                    value.forEach {  v ->
                        if (v == null) {
                            return@forEach
                        }
                        if (isSimpleType(v::class.java)) {
                            val newNode = BulletList()
                            newNode.chars = node.chars.replace(placeholder, v.toString())
                            node.insertBefore(newNode)
                        } else {
                            v::class.memberProperties.forEach {
                                println(node.chars)
                            }
                        }
                    }
                    node.unlink()
                } else {
                    // unsupported
                }
            }
        }


    }

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