package com.hupubao.dockit

import com.hupubao.dockit.parser.CommentParser
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.project.MavenProject
import java.io.File
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.nio.file.Paths


/**
 * @goal dockit
 */
@Mojo(name = "dockit", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
class DockitMojo : AbstractMojo() {

    @Parameter(defaultValue = "", property = "scanPackage", required = true)
    lateinit var scanPackage: String

    @Parameter(defaultValue = "DEFAULT", property = "template", required = true)
    lateinit var template: String

    @Parameter(defaultValue = "UTF-8", property = "templateCharset", required = true)
    lateinit var templateCharset: String

    @Parameter(defaultValue = "DEFAULT", property = "outDir", required = true)
    lateinit var outDir: String

    @Parameter(defaultValue = "\${project}", required = true, readonly = true)
    lateinit var project: MavenProject

    @Throws(MojoExecutionException::class, MojoFailureException::class)
    override fun execute() {
        log.info("[dockit]Start dockit......")
        log.info("[dockit]Scanning package [$scanPackage]...")
        log.info("[dockit]Project basedir:${project.basedir}")
        log.info("[dockit]Load template:$template")

        if (template == "DEFAULT") {
            template = "/template/DEFAULT.MD"
        }

        val templateText = DockitMojo::class.java.getResource(template).readText(Charset.forName(templateCharset))

        val classNodeList = CommentParser.parseComments(project, log)
        val loader = URLClassLoader(project.compileClasspathElements.map { element -> File(element.toString()).toURI().toURL() }.toTypedArray())

        println("load:" + loader.loadClass("cn.lamic.chagoi.beans.sys.ResponseBean"))

        if (outDir == "DEFAULT") {
            outDir = Paths.get(project.build.directory, "dockit").toString()
        }

        classNodeList.parallelStream().forEach { classNode ->

            val outDirectory = Paths.get(
                outDir,
                if (classNode.classDescription == null) classNode.className else classNode.classDescription
            ).toString()

            classNode.methodCommentNodeList.parallelStream().forEach { methodCommentNode ->

                val methodNameOrTitle =
                    if (methodCommentNode.title == null) methodCommentNode.methodName!! else methodCommentNode.title!!

                var mdText = templateText.replace("\${title}", methodNameOrTitle, false)
                    .replace(
                        "\${requestMethod}",
                        if (methodCommentNode.requestMethod == null) "" else methodCommentNode.requestMethod!!,
                        false
                    )
                    .replace(
                        "\${requestUrl}",
                        if (methodCommentNode.requestUrl == null) "" else methodCommentNode.requestUrl!!,
                        false
                    )

                var parameterText = ""
                var insertStartIndex = -1
                var insertEndIndex = -1
                mdText.split("\n").parallelStream().filter { line ->
                    line.contains("\${argName}")
                }.findFirst().ifPresent { lineParameter ->
                    insertStartIndex = mdText.indexOf(lineParameter)
                    insertEndIndex = insertStartIndex + lineParameter.length
                    methodCommentNode.requestArgList.parallelStream().forEachOrdered { requestParameter ->
                        parameterText += lineParameter.replace("\${paramName}", requestParameter.name!!, false)
                            .replace("\${argRequired}", requestParameter.required.toString(), false)
                            .replace("\${argType}", requestParameter.type!!, false)
                            .replace("\${argDescription}", requestParameter.description!!, false)
                    }
                }

                if (insertStartIndex > -1) {
                    mdText = mdText.substring(0, insertStartIndex) + parameterText + mdText.substring(insertEndIndex)
                }

                val pathOut = Paths.get(outDirectory, "$methodNameOrTitle.MD")
                val file = pathOut.toFile()
                /*if (file.exists()) {
                    file.delete()
                }*/

                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                file.createNewFile()
                log.info("[dockit]Generate doc to $pathOut")
                file.writeText(mdText)
            }

        }

    }

}