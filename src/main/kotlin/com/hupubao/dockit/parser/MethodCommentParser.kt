package com.hupubao.dockit.parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.javadoc.description.JavadocInlineTag
import com.hupubao.dockit.constants.TemplatePlaceholder
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.ArgumentType
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

            if (tagMethod.tagName == TemplatePlaceholder.version) {
                methodCommentNode.version = tagMethod.content.toText()
            }

            if (tagMethod.tagName == TemplatePlaceholder.status) {
                methodCommentNode.status = tagMethod.content.toText()
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
                parseNestedArgument(methodCommentNode, tagMethod, ArgumentType.REQUEST)
            }
            if (tagMethod.tagName == TemplatePlaceholder.resArg) {

                if (tagMethod.content.isEmpty) {
                    continue
                }

                parseNestedArgument(methodCommentNode, tagMethod, ArgumentType.RESPONSE)
            }


            if (tagMethod.type == JavadocBlockTag.Type.RETURN) {
                if (!tagMethod.content.isEmpty) {
                    val linkTypeStream = tagMethod.content.elements.stream().filter { javadocDescriptionElement ->
                        javadocDescriptionElement is JavadocInlineTag && javadocDescriptionElement.type == JavadocInlineTag.Type.LINKPLAIN
                    }.findFirst()

                    if (linkTypeStream.isPresent) {
                        val linkTypeTag = linkTypeStream.get() as JavadocInlineTag
                        methodCommentNode.responseObjectClassName = linkTypeTag.content.trim()
                    }
                }
            }

        }
        return Optional.of(methodCommentNode)
    }

    private fun parseArgument(tagMethod: JavadocBlockTag): Argument {
        val argText = tagMethod.content.toText()

        val argInfo = argText.split(",")
        if (argInfo.size < 4) {
            return Argument("", "Unknown", "请检查javadoc格式是否正确", "", "")
        }
        val argDescription =
            (if (argInfo.size > 4) argText.substring(argText.indexOf(argInfo[3])) else argInfo[3]).trim()

        val argName = argInfo[0].trim()

        val argType = if (argInfo.size > 1) {
            argInfo[1].trim()
        } else {
            "Unknown"
        }

        val argRequired = if (argInfo.size > 2) {
            argInfo[2].trim().replace("required=", "")
        } else {
            "No"
        }

        return com.hupubao.dockit.entity.Argument(
            argName,
            argName,
            argDescription,
            argRequired,
            argType
        )
    }


    private fun parseNestedArgument(
        methodCommentNode: MethodCommentNode,
        tagMethod: JavadocBlockTag,
        argumentType: ArgumentType
    ) {

        val argument = parseArgument(tagMethod)

        val argNameArr = argument.originName.split(".")

        // 解析JSONObject或JSONArray属性名
        if (argument.originName.contains(".")) {
            val parentProperty = argument.originName.substring(0, argument.originName.lastIndexOf("."))
            val property = argNameArr[argNameArr.size - 1]

            val parentArgument = findParentArgument(
                when (argumentType) {
                    ArgumentType.REQUEST -> methodCommentNode.requestArgList
                    else -> {
                        methodCommentNode.responseArgList
                    }
                }, parentProperty
            )


            if (parentArgument != null) {
                argument.name = property
                argument.level = argNameArr.size - 1
                argument.levelPrefix = " " + ("-".repeat(argument.level)) + " "
                parentArgument.children.add(argument)
            }

        } else {
            when (argumentType) {
                ArgumentType.REQUEST -> methodCommentNode.requestArgList.add(argument)
                else -> {
                    methodCommentNode.responseArgList.add(argument)
                }
            }
        }
    }

    private fun findParentArgument(argumentList: List<Argument>, parentProperty: String): Argument? {

        var argument = argumentList.find { argument -> argument.originName == parentProperty }

        if (argument != null) {
            return argument
        }

        argumentList.forEach { arg ->
            if (!arg.children.isEmpty()) {
                argument = findParentArgument(arg.children, parentProperty)

                if (argument != null) {
                    return argument
                }
            }
        }

        return null
    }
}