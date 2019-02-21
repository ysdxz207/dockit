package com.hupubao.dockit.resolver.template

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.ArgumentType
import com.hupubao.dockit.utils.MockUtils
import com.hupubao.dockit.utils.ProjectUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.random.Random

class MockDataResolver(private var project: MavenProject, private var log: Log) {


    private val sampleTypeArray =
        arrayOf("integer", "short", "long", "double", "float", "byte", "char", "string", "boolean")


    fun mockData(methodCommentNode: MethodCommentNode, argumentType: ArgumentType): String {
        val methodCommentNodeClone = methodCommentNode.clone()
        return mockArgData(
            when (argumentType) {
                ArgumentType.REQUEST -> methodCommentNodeClone.requestArgList
                else -> {
                    methodCommentNodeClone.responseArgList
                }
            }
        )
    }

    private fun mockArgData(argList: List<Argument>): String {

        val json = mockJSONObjectData(argList)

        return JSON.toJSONString(json, SerializerFeature.PrettyFormat)
    }

    private fun mockJSONObjectData(resArgList: List<Argument>): JSON {
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
                when {
                    argument.type.toLowerCase() == "array" -> {
                        val randomSize = Random.nextInt(1, 3)
                        val arr = JSONArray()
                        for (a in 0..randomSize) {
                            arr.add(mockJSONObjectData(argument.children))
                        }
                        data[argument.name] = arr
                    }
                    else -> data[argument.name] = mockJSONObjectData(argument.children)
                }
            }
        }
        return data
    }

    private fun getArgumentJavaType(argument: Argument): Optional<Class<*>> {
        if (sampleTypeArray.contains(argument.type.toLowerCase())) {
            return try {
                Optional.of(Class.forName("java.lang.${argument.type}"))
            } catch (e: Exception) {
                ProjectUtils.loadClass(project!!, log!!, argument.name)
            }
        }

        return Optional.empty()
    }

}