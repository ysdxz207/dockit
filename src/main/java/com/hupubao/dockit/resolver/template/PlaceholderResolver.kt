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

object PlaceholderResolver {
    private fun isSimpleType(clazz: Class<*>): Boolean {
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

    private fun resolveSimpleValue(node: Node, placeholderArr: List<String>, propertyValue: Any?) {

        for (placeholder in placeholderArr) {
            val placeholderUndressed = undressPlaceholder(placeholder)
            var value = ""
            if (propertyValue != null) {
                value = propertyValue.toString()
            }
            node.chars = node.chars.replace("\${$placeholderUndressed}", value)

        }

    }

    fun resolve(node: Node, placeholderList: List<String>, templateValue: Any) {

        val nodesToUnlink = mutableListOf<Node>()

        if (isSimpleType(templateValue::class.java)) {
            resolveSimpleValue(node, placeholderList, templateValue)
            return
        }
        for (property in templateValue::class.memberProperties) {

            val placeholder = placeholderList.find { placeholder ->
                val annotation = property.javaField?.getAnnotation(Placeholder::class.java)
                annotation?.value == undressPlaceholder(placeholder).split(".")[0]
            } ?: continue

            val placeholderArray = undressPlaceholder(placeholder).split(".")
            val isCorrectListPlaceholder = placeholderArray.size > 1
            val annotation = property.javaField!!.getAnnotation(Placeholder::class.java) ?: continue
            val propertyValue = property.getter.call(templateValue)

            if (annotation.type == PlaceholderType.SIMPLE) {
                resolveSimpleValue(node, placeholderArray, propertyValue)
                continue
            }

            if (annotation.type == PlaceholderType.LIST) {

                if (propertyValue is Iterable<*>) {
                    if (node is Paragraph) {
                        val tableItemText = node.lastChild
                        propertyValue.forEach { argument ->
                            if (argument == null) {
                                return@forEach
                            }
                            val newText = Text()
                            val newTextChars = StringBuilder(tableItemText.chars.replace("${annotation.value}.", ""))
                            if (!newTextChars.endsWith(BasedSequence.EOL)) {
                                newTextChars.append(BasedSequence.EOL_CHARS)
                            }
                            newText.chars = SubSequence.of(newTextChars)
                            tableItemText.insertBefore(newText)
                            for (p in argument::class.memberProperties) {
                                resolveSimpleValue(newText, mutableListOf(p.name), p.getter.call(argument))
                            }
                        }
                        if (!nodesToUnlink.contains(tableItemText)) {
                            nodesToUnlink.add(tableItemText)
                        }
                    } else {

                        propertyValue.forEach { f ->
                            val newNode = BulletList()
                            newNode.chars = node.chars
                            node.insertBefore(newNode)
                            resolveSimpleValue(newNode, if (isCorrectListPlaceholder) mutableListOf(placeholderArray[1]) else mutableListOf(placeholderArray[0]), f ?: "")
                        }
                        if (!nodesToUnlink.contains(node)) {
                            nodesToUnlink.add(node)
                        }
                    }
                } else {
                    resolveSimpleValue(node, placeholderArray, propertyValue)
                }
            }
        }

        nodesToUnlink.forEach { n ->
            val parent = n.parent
            n.unlink()
            if (parent != null) {
                val nodeChars = StringBuilder()
                parent.children.forEach { child ->

                    nodeChars.append(child.chars)
                }
                parent.chars = SubSequence.of(nodeChars)
            }
        }

    }

    private fun undressPlaceholder(placeholder: String): String {
        return placeholder.replace("\${", "")
            .replace("}", "")
    }
}