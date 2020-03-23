package com.hupubao.dockit.template

import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.enums.ArgumentType
import com.hupubao.dockit.resolver.template.MockDataResolver
import com.hupubao.dockit.resolver.template.PlaceholderResolver
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

open class Template(
    private var project: MavenProject, private var log: Log, var source: String,
    private var methodCommentNode: MethodCommentNode
) {
    lateinit var document: Node


    init {
        this.parse()
    }


    private fun parse() {
        // 返回结果json示例
        methodCommentNode.reqSample = MockDataResolver(project, log).mockData(methodCommentNode, ArgumentType.REQUEST)
        methodCommentNode.resSample = MockDataResolver(project, log).mockData(methodCommentNode, ArgumentType.RESPONSE)

        // 解析模版前需要先把参数名拼接前缀
        this.resolveArgumentsPrefix(methodCommentNode.requestArgList)
        this.resolveArgumentsPrefix(methodCommentNode.responseArgList)

        document = Parser.builder().build().parse(source)!!
        for (node in document.children) {
            val matchResult = ("""\$\{\w+\.*\w+\}""".toRegex()).findAll(node.chars.toString())
            if (matchResult.none()) {
                continue
            }

            if (matchResult.count() == 1) {
                PlaceholderResolver.resolve(node, mutableListOf(matchResult.single().value), this.methodCommentNode)
            } else {
                val arr = mutableListOf<String>()
                matchResult.forEach { p ->
                    arr.add(p.value)
                }
                PlaceholderResolver.resolve(node, arr, this.methodCommentNode)
            }
        }
    }

    private fun resolveArgumentsPrefix(resArgList: List<Argument>) {

        resArgList.forEach { argument ->
            argument.name = argument.levelPrefix + argument.name
            if (!argument.children.isEmpty()) {
                resolveArgumentsPrefix(argument.children)
            }
        }
    }



    fun render(): String {

        val sb = StringBuilder()
        for (node in document.children) {
            sb.append(node.chars).append(BasedSequence.EOL_CHARS)
            if (!node.chars.endsWith(BasedSequence.EOL)) {
                sb.append(BasedSequence.EOL_CHARS)
            }
        }

        return sb.toString()
    }
}