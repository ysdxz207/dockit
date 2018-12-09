package com.hupubao.dockit.utils

fun main(args: Array<String>) {

    var text = "a\nb\n"
    var str = ""
    var list = listOf("1", "2", "3")

    text.split("\n").parallelStream().filter {
        it.contains("b")
    }.findFirst().ifPresent {
        list.parallelStream().forEachOrdered {
            str += it.replace("", "")
                    .replace("","")
        }
    }

    println(str)

}