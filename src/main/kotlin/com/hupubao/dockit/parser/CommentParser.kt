package com.hupubao.dockit.parser

import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.javaparser.utils.SourceRoot
import com.hupubao.dockit.entity.ClassNode
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.nio.file.Paths

object CommentParser: ClassCommentParser() {
    fun parseComments(project: MavenProject, log: Log): List<ClassNode> {
        val classNodeList: MutableList<ClassNode> = mutableListOf()
        project.compileSourceRoots.forEach {
            val projectRoot = ParserCollectionStrategy().collect(Paths.get(it.toString()))
            val sourceRoot = SourceRoot(projectRoot.root)

            val parseResultList: List<ParseResult<CompilationUnit>> = sourceRoot.tryToParse()

            parseResultList.stream().forEach { parseResult ->
                parseResult.ifSuccessful { compilationUnit ->
                    compilationUnit.types.parallelStream().forEach { typeDeclaration ->
                        parse(project, log, typeDeclaration).ifPresent { classNode ->
                            classNodeList.add(classNode)
                        }
                    }
                }

            }
        }
        return classNodeList
    }
}