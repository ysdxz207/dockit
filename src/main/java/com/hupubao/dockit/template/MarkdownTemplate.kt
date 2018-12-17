package com.hupubao.dockit.template

import com.hupubao.dockit.entity.MethodCommentNode
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

class MarkdownTemplate(project: MavenProject, log: Log, source: String, methodCommentNode: MethodCommentNode) : Template(project, log, source, methodCommentNode) {


    fun toHtml() {

    }

}