package com.hupubao.dockit

import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.javaparser.utils.SourceRoot
import com.hupubao.dockit.entity.CommentNode
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.nio.file.Paths


/**
 * @goal dockit
 */
@Mojo(name = "dockit", defaultPhase = LifecyclePhase.COMPILE)
class DockitMojo : AbstractMojo() {

    @Parameter(defaultValue = "", property = "scanPackage", required = true)
    lateinit var scanPackage: String

    @Parameter(defaultValue = "\${project}", required = true, readonly = true)
    lateinit var project: MavenProject

    @Throws(MojoExecutionException::class, MojoFailureException::class)
    override fun execute() {
        log.info("[dockit]Start dockit......")
        log.info("[dockit]Scanning package [$scanPackage]...")
        log.info("[dockit]Project basedir:${project.basedir}")

        val projectRoot = ParserCollectionStrategy().collect(Paths.get("${project.basedir}/src/main/java"))
        val sourceRoot = SourceRoot(projectRoot.root)

        val parseResultList: List<ParseResult<CompilationUnit>> = sourceRoot.tryToParse()

        parseResultList.stream().forEach { parseResult ->
            parseResult.ifSuccessful {
                it.types.parallelStream().forEach { typeDeclaration ->

                    typeDeclaration.javadocComment.ifPresent { javadocComment ->
                        val dockit = javadocComment.parse().blockTags.lastOrNull { tag ->
                            tag.tagName == "dockit"
                        } ?: return@ifPresent
                        // class has comment and has tag dockit
                        val dockitText = dockit.content.toText()
                        println(dockitText)

                        typeDeclaration.childNodes.parallelStream().forEach { node ->
                            node.comment.ifPresent { comment ->
                                comment.ifJavadocComment { javadocCommentMethod ->
                                    val commentNode = CommentNode()
                                    for (tagMethod in javadocCommentMethod.parse().blockTags) {
                                        if (tagMethod.tagName == "description") {
                                            commentNode.description = tagMethod.content.toText()
                                        }

                                        if (tagMethod.tagName == "requestMethod") {
                                            commentNode.requestMethod = tagMethod.content.toText()
                                        }

                                        if (tagMethod.type == JavadocBlockTag.Type.PARAM) {
                                            val paramName = if (tagMethod.name.isPresent) {
                                                tagMethod.name.get()
                                            } else {
                                                ""
                                            }
                                            val paramDescription = if(tagMethod.content.elements.isEmpty()) {
                                                paramName
                                            } else {
                                                tagMethod.content.elements[0].toText()
                                            }
                                            commentNode.requestParameterList.add(com.hupubao.dockit.entity.Parameter(paramName, paramDescription))
                                        }
                                    }

                                    println(commentNode)

                                }

                            }
                        }

                    }
                }
            }
        }
    }

}