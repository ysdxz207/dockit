package com.hupubao.dockit.parser

import com.github.javaparser.ast.body.TypeDeclaration
import com.hupubao.dockit.entity.ClassNode
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.util.*

open class ClassCommentParser : MethodCommentParser() {

    fun parse(project: MavenProject, log: Log, typeDeclaration: TypeDeclaration<*>): Optional<ClassNode> {
        if (!typeDeclaration.hasJavaDocComment()) {
            return Optional.empty()
        }

        val dockit = typeDeclaration.javadocComment.get().parse().blockTags.lastOrNull { tag ->
            tag.tagName == "dockit"
        } ?: return Optional.empty()


        val classNode = ClassNode()
        classNode.className = typeDeclaration.nameAsString
        // class has comment and has tag dockit
        classNode.classDescription = dockit.content.toText()

        typeDeclaration.childNodes.parallelStream().forEach { methodNode ->
            parse(project, log, methodNode).ifPresent { methodCommentNode ->
                classNode.methodCommentNodeList.add(methodCommentNode)
            }
        }


        return Optional.of(classNode)
    }
}



