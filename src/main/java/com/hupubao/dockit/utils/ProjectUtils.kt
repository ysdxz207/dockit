package com.hupubao.dockit.utils

import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.File
import java.net.URLClassLoader
import java.util.*

/**
 * @author ysdxz207
 */
object ProjectUtils {

    /**
     * Get class load from project and load class.
     */
    fun loadClass(project: MavenProject, log: Log, name: String): Optional<Class<*>> {
        val loader = URLClassLoader(project.compileClasspathElements.map { element -> File(element.toString()).toURI().toURL() }.toTypedArray())
        return try {
            Optional.of(loader.loadClass(name))
        } catch (e: ClassNotFoundException) {
            log.warn("[dockit]Can not find class:$name")
            Optional.empty()
        } catch (e: Throwable) {
            Optional.empty()
        }
    }
}
