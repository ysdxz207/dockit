package com.hupubao.dockit.utils

import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.javaparser.utils.SourceRoot
import com.hupubao.dockit.entity.ClassNode
import com.hupubao.dockit.entity.MethodCommentNode
import org.apache.maven.project.MavenProject
import java.nio.file.Paths

object CommentUtils {
    fun parseComments(project: MavenProject): List<ClassNode> {
        val classNodeList: MutableList<ClassNode> = mutableListOf()
        project.compileSourceRoots.forEach {
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

                                            if (tagMethod.tagName == "arg") {

                                                if (tagMethod.content.isEmpty) {
                                                    continue
                                                }

                                                val argText = tagMethod.content.toText()

                                                val argSplitIndex = argText.indexOf(" ")
                                                val argInfo = argText.substring(0, argSplitIndex).split(",")
                                                val argDescription = argText.substring(argSplitIndex)

                                                val argName = argInfo[0]

                                                val argType = if (argInfo.size > 1) {
                                                    argInfo[1]
                                                } else {
                                                    "Object"
                                                }

                                                val argRequired = if (argInfo.size > 2) {
                                                    argInfo[2] == "required"
                                                } else {
                                                    false
                                                }

                                                methodCommentNode.requestParameterList.add(com.hupubao.dockit.entity.Parameter(argName, argDescription, argRequired, argType))
                                            }

                                            if (tagMethod.type == JavadocBlockTag.Type.RETURN) {

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
        }
        return classNodeList
    }
}