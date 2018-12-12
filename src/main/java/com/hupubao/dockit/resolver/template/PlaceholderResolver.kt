package com.hupubao.dockit.resolver.template

import com.hupubao.dockit.entity.ClassNode
import com.hupubao.dockit.template.MarkdownTemplate
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

    fun resolve(document: Node, property: String, value: Any?) {

        if (value == null) {
            return
        }
        val placeholder = "\${$property}"
        document.children.map { node ->
            if (node.chars.contains(placeholder)) {
                if (isSimpleType(value::class.java)) {
                    if (node != null) {
                        node.chars = node.chars.replace(placeholder, value.toString())
                    }
                } else if (value is Iterable<*>) {
                    // list

                } else {
                    // unsupported
                }
            }
        }


    }

    @JvmStatic
    fun main(args: Array<String>) {
        val text = javaClass.getResource("/template/DEFAULT.MD").readText(Charset.forName("UTF-8"))

        val classNode = ClassNode()
        classNode.classDescription = "大表哥"
        MarkdownTemplate(text, classNode).render()
    }
}