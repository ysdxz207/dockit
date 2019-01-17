package com.hupubao.dockit.resolver.template

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.BodyDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.comments.Comment
import com.github.javaparser.utils.ParserCollectionStrategy
import com.github.jsonzou.jmockdata.JMockData
import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.utils.MockUtils
import com.hupubao.dockit.utils.ProjectUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import kotlin.random.Random

class MockDataResolver(private var project: MavenProject, private var log: Log) {


    private val sampleTypeArray =
        arrayOf("integer", "short", "long", "double", "float", "byte", "char", "string", "boolean")


    fun mockResponseData(methodCommentNode: MethodCommentNode): String {
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

            parseResponseArgList(project, methodCommentNodeClone)
        }
        result = mockData(methodCommentNodeClone.responseArgList)

        return result
    }

    private fun parseResponseArgList(
        project: MavenProject,
        methodCommentNodeClone: MethodCommentNode
    ) {

        // parse javadoc
        // 先找到project的目录，再拼上包路径，找java源文件，再进行JavaParser.parse
        project.compileSourceRoots.forEach {
            val projectRoot = ParserCollectionStrategy().collect(Paths.get(it.toString()))
            val filePath =
                projectRoot.root.toString() +
                        "/" + methodCommentNodeClone.responseObjectClassName!!.replace(
                    ".",
                    "/"
                ) + ".java"

            val file = File(filePath)
            val compilationUnit = JavaParser.parse(file)
            compilationUnit.comments.forEach { comment ->
                comment.commentedNode.ifPresent { commentNode ->
                    if (commentNode is FieldDeclaration && commentNode.childNodes.size > 0) {

                        val arg = commentNode.childNodes[0]
                        val javaDoc = comment.asJavadocComment().parse()
                        val required =
                            javaDoc.blockTags.find { tag -> tag.tagName == "required" }?.content?.toText() ?: "Yes"
                        if (arg is VariableDeclarator) {
                            var type = arg.typeAsString
                            if (!type.startsWith("java.lang.")) {
                                type = "Object"
                            }
                            val argument = Argument(
                                arg.nameAsString,
                                arg.nameAsString,
                                javaDoc.description.toText(),
                                required,
                                type
                            )
                            methodCommentNodeClone.responseArgList.add(argument)
                        }
                    }
                }
            }
        }
    }

    private fun mockData(resArgList: List<Argument>): String {

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