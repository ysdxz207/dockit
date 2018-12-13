package com.hupubao.dockit.template

import com.vladsch.flexmark.parser.Parser
import java.nio.charset.Charset
fun main(args: Array<String>) {

    Test.test()
}

object Test {

    fun test() {
        val document = Parser.builder().build().parse(Test::class.java.getResource("/template/DEFAULT.MD").readText(Charset.forName("UTF-8")))
        document.children.forEach { node ->
            ("""\$\{\w+\.*\w+\}""".toRegex()).findAll(node.chars.toString()).forEach {


            }

        }
    }
}