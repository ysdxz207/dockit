package com.hupubao.dockit

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.utils.CommentUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.*
import org.apache.maven.plugin.descriptor.PluginDescriptor
import org.codehaus.plexus.classworlds.UrlUtils.getURLs






/**
 * @goal dockit
 * @requiresDependencyResolution compile
 * requiresDependencyResolution:https://maven.apache.org/developers/mojo-api-specification.html
 */
@Mojo(name = "dockit", defaultPhase = LifecyclePhase.COMPILE)
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

    @Parameter(defaultValue = "\${plugin}", readonly = true)
    lateinit var pluginDescriptor: PluginDescriptor

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

        val classNodeList = CommentUtils.parseComments(project)


        if (outDir == "DEFAULT") {
            outDir = Paths.get(project.build.directory, "dockit").toString()
        }

        classNodeList.parallelStream().forEach { classNode ->

            val outDirectory = Paths.get(outDir, if (classNode.classDescription == null) classNode.className else classNode.classDescription).toString()
            classNode.methodCommentNodeList.parallelStream().forEach { methodCommentNode ->

                val methodNameOrDesc = if (methodCommentNode.description == null) methodCommentNode.methodName!! else methodCommentNode.description!!

                var mdText = templateText.replace("\${description}", methodNameOrDesc, false)
                    .replace("\${requestMethod}", if (methodCommentNode.requestMethod == null) "" else methodCommentNode.requestMethod!!, false)
                    .replace("\${requestUrl}", if (methodCommentNode.requestUrl == null) "" else methodCommentNode.requestUrl!!, false)

                var parameterText = ""
                var insertStartIndex = -1
                var insertEndIndex = -1
                mdText.split("\n").parallelStream().filter { line ->
                    line.contains("\${paramName}")
                }.findFirst().ifPresent { lineParameter ->
                    insertStartIndex = mdText.indexOf(lineParameter)
                    insertEndIndex = insertStartIndex + lineParameter.length
                    methodCommentNode.requestParameterList.parallelStream().forEachOrdered { requestParameter ->
                        parameterText += lineParameter.replace("\${paramName}", requestParameter.paramName!!, false)
                            .replace("\${paramRequired}", requestParameter.required.toString(), false)
                            .replace("\${paramType}", requestParameter.type!!, false)
                            .replace("\${paramDescription}", requestParameter.paramDescription!!, false)
                    }
                }

                if (insertStartIndex > -1) {
                    mdText = mdText.substring(0, insertStartIndex) + parameterText + mdText.substring(insertEndIndex)
                }

                val pathOut = Paths.get(outDirectory, "$methodNameOrDesc.MD")
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

        val pluginDescriptor = pluginContext["pluginDescriptor"] as PluginDescriptor
        // printing the ClassRealm content containing plugin classpath dependencies
        val classRealm = pluginDescriptor.classRealm

        classRealm.addURL(File(project.build.outputDirectory).toURI().toURL())
        project.dependencyArtifacts.forEach { element -> classRealm.addURL(URL("file:$element")) }
        for (url in classRealm.urLs) log.info(" >>> " + url.toString())

        val jMockData = JMockData.mock(Class.forName("cn.lamic.chagoi.beans.sys.ResponseBean"))
        println(JSON.toJSONString(jMockData, SerializerFeature.PrettyFormat))
//        val projectClasspathList = ArrayList<URL>()
//        project.dependencyArtifacts.forEach { element -> projectClasspathList.add(URL("file:$element")) }
//        println(JSON.toJSONString(projectClasspathList))
//        val loader = URLClassLoader(projectClasspathList.toTypedArray())
//        val jMockData = JMockData.mock(loader.loadClass("cn.lamic.chagoi.beans.sys.ResponseBean"))
//        println(JSON.toJSONString(jMockData, SerializerFeature.PrettyFormat))

    }

}