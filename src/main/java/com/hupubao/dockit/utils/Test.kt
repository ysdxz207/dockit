package com.hupubao.dockit.utils

import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.javaparser.utils.SourceRoot
import com.hupubao.dockit.entity.ClassNode
import com.hupubao.dockit.entity.MethodCommentNode
import java.nio.file.Paths

fun main(args: Array<String>) {
    val classNodeList: MutableList<ClassNode> = mutableListOf()

    val it = "D://workspace/idea/hupubao/src/main/java"
    val projectRoot = ParserCollectionStrategy().collect(Paths.get(it.toString()))
    val sourceRoot = SourceRoot(projectRoot.root)

    val parseResultList: List<ParseResult<CompilationUnit>> = sourceRoot.tryToParse()

    parseResultList.stream().forEach { parseResult ->
        parseResult.ifSuccessful {

            it.types.parallelStream().forEach { typeDeclaration ->

                val classNode = ClassNode()
                classNode.className = typeDeclaration.nameAsString
                typeDeclaration.javadocComment.ifPresent { javadocComment ->
                    val dockit = javadocComment.parse().blockTags.lastOrNull { tag ->
                        tag.tagName == "dockit"
                    } ?: return@ifPresent
                    // class has comment and has tag dockit
                    classNode.classDescription = dockit.content.toText()

                    typeDeclaration.childNodes.parallelStream().forEach { methodNode ->

                        if (methodNode !is MethodDeclaration) {
                            return@forEach
                        }

                        methodNode.comment.ifPresent { comment ->

                            comment.ifJavadocComment { javadocCommentMethod ->
                                val methodCommentNode = MethodCommentNode()
                                methodCommentNode.methodName = methodNode.name.toString()
                                for (tagMethod in javadocCommentMethod.parse().blockTags) {
                                    if (tagMethod.tagName == "description") {
                                        methodCommentNode.description = tagMethod.content.toText()
                                    }

                                    if (tagMethod.tagName == "requestMethod") {
                                        methodCommentNode.requestMethod = tagMethod.content.toText()
                                    }

                                    if (tagMethod.type == JavadocBlockTag.Type.PARAM) {

                                        if (!tagMethod.name.isPresent) {
                                            continue
                                        }
                                        val nameArray: List<String> = tagMethod.name.get().split(",")


                                        val paramName = nameArray[0]
                                        val paramDescription = if(tagMethod.content.elements.isEmpty()) {
                                            paramName
                                        } else {
                                            tagMethod.content.elements[0].toText()
                                        }

                                        val paramType = if (nameArray.size > 1) {
                                            nameArray[1]
                                        } else {
                                            "Object"
                                        }

                                        val paramRequired = if (nameArray.size > 2) {
                                            nameArray[2] == "required"
                                        } else {
                                            false
                                        }

                                        methodCommentNode.requestParameterList.add(com.hupubao.dockit.entity.Parameter(paramName, paramDescription, paramRequired, paramType))
                                    }

                                }
                                classNode.methodCommentNodeList.add(methodCommentNode)

                            }

                        }
                    }

                    classNodeList.add(classNode)
                }

            }
        }

    }
    println(classNodeList)
}