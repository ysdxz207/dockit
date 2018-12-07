package com.hupubao.dockit

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File


/**
 * @goal dockit
 *
 *
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

        log.info(MdDoclet::class.java.getResource("").path)
        for (sourcePath in project.compileSourceRoots) {
            val scanPath = sourcePath.toString().replace("\\", "/") + "/" + scanPackage.replace(".", "/")

            File(scanPath).walkBottomUp().forEach {
                if (it.isFile) {
                }
            }
        }

    }

}