package com.hupubao.dockit.parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.javadoc.description.JavadocInlineTag
import com.hupubao.dockit.constants.TemplatePlaceholder
import com.hupubao.dockit.entity.MethodCommentNode
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.util.*

open class MethodCommentParser {

    fun parse(project: MavenProject, log: Log, methodNode: Node): Optional<MethodCommentNode> {
        if (methodNode !is MethodDeclaration
            || !methodNode.comment.isPresent
            || !methodNode.hasJavaDocComment()
        ) {
            return Optional.empty()
        }
        val methodCommentNode = MethodCommentNode()
        methodCommentNode.methodName = methodNode.name.toString()
        for (tagMethod in methodNode.javadocComment.get().parse().blockTags) {
            if (tagMethod.tagName == TemplatePlaceholder.title) {
                methodCommentNode.title = tagMethod.content.toText()
            }

            if (tagMethod.tagName == TemplatePlaceholder.desc) {
                methodCommentNode.descriptionList.add(tagMethod.content.toText())
            }

            if (tagMethod.tagName == TemplatePlaceholder.url) {
                methodCommentNode.requestUrl = tagMethod.content.toText()
            }

            if (tagMethod.tagName == TemplatePlaceholder.method) {
                methodCommentNode.requestMethod = tagMethod.content.toText()
            }

            if (tagMethod.tagName == TemplatePlaceholder.remark) {
                methodCommentNode.remark = tagMethod.content.toText()
            }

            if (tagMethod.tagName == TemplatePlaceholder.arg) {

                if (tagMethod.content.isEmpty) {
                    continue
                }

                val argText = tagMethod.content.toText()

                val argSplitIndex = argText.indexOf(" ")
                val argInfo = argText.substring(0, argSplitIndex).split(",")
                val argDescription = argText.substring(argSplitIndex)

                val argName = argInfo[0].trim()

                val argType = if (argInfo.size > 1) {
                    argInfo[1].trim()
                } else {
                    "Object"
                }

                val argRequired = if (argInfo.size > 2) {
                    argInfo[2].trim().replace("required=", "")
                } else {
                    "No"
                }

                methodCommentNode.requestArgList.add(
                    com.hupubao.dockit.entity.Argument(
                        argName,
                        argDescription,
                        argRequired,
                        argType
                    )
                )
            }

            if (tagMethod.type == JavadocBlockTag.Type.RETURN) {
                if (!tagMethod.content.isEmpty) {
                    val linkTypeStream = tagMethod.content.elements.stream().filter { javadocDescriptionElement ->
                        javadocDescriptionElement is JavadocInlineTag && javadocDescriptionElement.type == JavadocInlineTag.Type.LINK
                    }.findFirst()

                    if (linkTypeStream.isPresent) {
                        val linkTypeTag = linkTypeStream.get() as JavadocInlineTag
                        methodCommentNode.responseObjectClassName = linkTypeTag.content
                    }
                }
            }

        }
        return Optional.of(methodCommentNode)
    }
}