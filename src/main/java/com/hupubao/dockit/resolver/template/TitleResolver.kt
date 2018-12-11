package com.hupubao.dockit.resolver.template

import org.commonmark.node.Node

object TitleResolver {

    fun resolve(document: Node, title: String) {
        val next = document.next
        while (next != null) {
            next.accept()
        }
    }
}