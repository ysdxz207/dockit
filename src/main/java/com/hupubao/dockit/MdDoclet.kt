package com.hupubao.dockit

import com.sun.javadoc.ClassDoc
import com.sun.javadoc.Doclet
import com.sun.javadoc.RootDoc


class MdDoclet : Doclet() {

    companion object {

        fun start(root: RootDoc): Boolean {
            writeContents(root.classes(), "dockit")
            for (classDoc in root.classes()) {
                println("Class: " + classDoc.qualifiedName())

                for (methodDoc in classDoc.methods()) {
                    println("  " + methodDoc.returnType() + " " + methodDoc.name() + methodDoc.signature())
                }
            }
            return true
        }

        private fun writeContents(classes: Array<ClassDoc>, tagName: String) {
            for (i in classes.indices) {
                var classNamePrinted = false
                val methods = classes[i].methods()
                for (j in methods.indices) {
                    val tags = methods[j].tags(tagName)
                    if (tags.isNotEmpty()) {
                        if (!classNamePrinted) {
                            println("\n" + classes[i].name() + "\n")
                            classNamePrinted = true
                        }
                        println(methods[j].name())
                        for (k in tags.indices) {
                            System.out.println(
                                "   " + tags[k].name() + ": "
                                        + tags[k].text()
                            )
                        }
                    }
                }
            }
        }
    }

}