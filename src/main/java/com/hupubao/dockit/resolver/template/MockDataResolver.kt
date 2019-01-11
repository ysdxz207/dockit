package com.hupubao.dockit.resolver.template

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.utils.MockUtils
import com.hupubao.dockit.utils.ProjectUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.util.*
import java.util.stream.Collectors
import kotlin.random.Random

class MockDataResolver(project: MavenProject?, log: Log?) {


    private var project: MavenProject? = project
    private var log: Log? = log

    private val SAMPLE_TYPE_ARRAY =
        arrayOf("integer", "short", "long", "double", "float", "byte", "char", "string", "boolean")


    public fun mockResponseData(methodCommentNode: MethodCommentNode): String {
        var result = ""
        val methodCommentNodeClone = methodCommentNode.clone()
        if (methodCommentNodeClone.responseObjectClassName != null) {
            val isList: Boolean
            val subClassName: String = if (methodCommentNodeClone.responseObjectClassName!!.contains("<")) {
                // list
                isList = true
                methodCommentNodeClone.responseObjectClassName!!.substring(
                    methodCommentNodeClone.responseObjectClassName!!.indexOf("<") + 1,
                    methodCommentNodeClone.responseObjectClassName!!.indexOf(">")
                )
            } else {
                // object
                isList = false
                methodCommentNodeClone.responseObjectClassName!!
            }

            val clazzOptional = ProjectUtils.loadClass(project!!, log!!, methodCommentNodeClone.responseObjectClassName!!)
            clazzOptional.ifPresent { clazz ->
                if (isList) {

                }
                methodCommentNodeClone.responseArgList = Arrays.stream(clazz.declaredFields).map { f ->
                    // parse javadoc

                    Argument(f.name, f.name, f.name, "Yes", f.type.simpleName)
                }.collect(Collectors.toList())
                result = mockJSONObjectData(clazz)
            }
        } else {
            result = mockData(methodCommentNodeClone.responseArgList)
        }

        return result
    }

    private fun mockData(resArgList: List<Argument>): String {

        val isArray = resArgList.size == 1

        if (isArray) {
            val randomSize = Random.nextInt(1, 3)
            val arr = JSONArray()
            for (a in 1..randomSize) {
                arr.add(mockJSONObjectData(resArgList))
            }
            return JSON.toJSONString(arr, SerializerFeature.PrettyFormat)
        }

        return JSON.toJSONString(mockJSONObjectData(resArgList), SerializerFeature.PrettyFormat)
    }

    private fun mockJSONObjectData(resArgList: List<Argument>): JSONObject {
        val data = JSONObject()
        resArgList.forEach { argument ->
            // 简单类型
            if (argument.children.isEmpty()) {
                val typeOptional = getArgumentJavaType(argument)

                var value: Any = MockUtils.mockFromEnglishSeed()
                typeOptional.ifPresent { type ->
                    value = JMockData.mock(type)
                }
                data[argument.name] = value

            } else {
                // 对象或数组
                if (argument.type.toLowerCase() == "array") {
                    val randomSize = Random.nextInt(1, 3)
                    val arr = JSONArray()
                    for (a in 0..randomSize) {
                        arr.add(mockJSONObjectData(argument.children))
                    }
                    data[argument.name] = arr
                } else {
                    data[argument.name] = mockJSONObjectData(argument.children)
                }
            }
        }
        return data
    }

    private fun getArgumentJavaType(argument: Argument): Optional<Class<*>> {
        if (SAMPLE_TYPE_ARRAY.contains(argument.type.toLowerCase())) {
            return try {
                Optional.of(Class.forName("java.lang.${argument.type}"))
            } catch (e: Exception) {
                ProjectUtils.loadClass(project!!, log!!, argument.name)
            }
        }

        return Optional.empty()
    }

    private fun mockJSONObjectData(clazz: Class<*>): String {
        val data = if (clazz.newInstance() is Iterable<*>) {
            mutableListOf(clazz)
        } else {
            JMockData.mock(clazz)
        }
        return JSON.toJSONString(data, SerializerFeature.PrettyFormat)
    }
}