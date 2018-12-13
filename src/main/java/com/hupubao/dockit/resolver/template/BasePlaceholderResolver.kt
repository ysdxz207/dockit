package com.hupubao.dockit.resolver.template

import com.hupubao.dockit.annotation.Placeholder
import com.hupubao.dockit.enums.PlaceholderType
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ast.Text
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.SubSequence
import java.lang.StringBuilder
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

open class BasePlaceholderResolver {
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

    private fun resolveSimpleValue(node: Node, placeholderUndressed: String, propertyValue: Any?) {
        var value = ""
        if (propertyValue != null) {
            value = propertyValue.toString()
        }
        val charsWithValue = node.chars.replace("\${$placeholderUndressed}", value)
        if (!charsWithValue.endsWith(BasedSequence.EOL)) {
            charsWithValue.append(BasedSequence.EOL_CHARS)
        }
        node.chars = charsWithValue
    }

    fun resolve(node: Node, placeholder: String, templateValue: Any) {

        val placeholderUndressed = placeholder.replace("\${", "")
            .replace("}", "")
        if (isSimpleType(templateValue::class.java)) {
            resolveSimpleValue(node, placeholderUndressed, templateValue)
            return
        }
        for (property in templateValue::class.memberProperties) {
            val placeholderArray = placeholderUndressed.split(".")
            val isCorrectListPlaceholder = placeholderArray.size > 1
            val field = property.javaField ?: continue
            val annotation = field.getAnnotation(Placeholder::class.java) ?: continue
            if (annotation.value != placeholderArray[0]) {
                continue
            }
            val propertyValue = property.getter.call(templateValue)

            if (annotation.type == PlaceholderType.SIMPLE) {
                resolveSimpleValue(node, placeholderUndressed, propertyValue)
                continue
            }

            if (annotation.type == PlaceholderType.LIST) {

                if (propertyValue is Iterable<*>) {
                    if (node is Paragraph) {
                        val tableItemText = node.children.last()
                        propertyValue.forEach { argument ->
                            if (argument == null) {
                                return@forEach
                            }
                            val newText = Text()
                            newText.chars = tableItemText.chars.replace("${annotation.value}.", "")
                            for (p in argument::class.memberProperties) {
                                resolveSimpleValue(newText, p.name, p.getter.call(argument))
                            }
                            tableItemText.insertBefore(newText)
                        }

                    } else {

                        propertyValue.forEach { f ->
                            val newNode = BulletList()
                            newNode.chars = node.chars
                            resolve(newNode, if (isCorrectListPlaceholder) placeholderArray[1] else placeholderArray[0], f ?: "")
                            node.insertBefore(newNode)
                        }
                    }
                } else {
                    resolveSimpleValue(node, placeholderUndressed, propertyValue)
                }
            }

        }

    }
}