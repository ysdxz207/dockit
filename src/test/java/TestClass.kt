import java.nio.file.Paths

object TestClass {
    @JvmStatic
    fun main(args: Array<String>) {

        val singleOutDir = "true"
        val outDirectory = Paths.get(
            "D:/",
            if ("true" == singleOutDir.toLowerCase()) "" else "aa"
        ).toString()
        println(outDirectory)
    }
}
