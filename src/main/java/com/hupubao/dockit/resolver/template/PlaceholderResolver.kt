package com.hupubao.dockit.resolver.template

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import java.nio.charset.Charset


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
        val node = document.children.find{ node -> node.chars.contains(placeholder) }
        if (isSimpleType(value::class.java)) {
            if (node != null) {
                node.chars = node.chars.replace(placeholder, value.toString())
                println(node.chars)
            }
        } else {
            // list

        }
    }
    @JvmStatic
    fun main(args: Array<String>) {
        val text = javaClass.getResource("/template/DEFAULT.MD").readText(Charset.forName("UTF-8"))
        val document = Parser.builder().build().parse(text)
        PlaceholderResolver.resolve(document, "title", "我就是大标题")
    }
}