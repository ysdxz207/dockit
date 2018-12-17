import com.hupubao.dockit.entity.Argument
import com.hupubao.dockit.entity.MethodCommentNode
import com.hupubao.dockit.template.MarkdownTemplate
import org.junit.Test
import java.nio.charset.Charset


class TestPlaceholderResolver {



//    @Test
    fun test() {
        val text = javaClass.getResource("/template/DEFAULT.MD").readText(Charset.forName("UTF-8"))

        val methodCommentNode = MethodCommentNode()
        methodCommentNode.title = "大表哥"
        methodCommentNode.descriptionList.add("描述1")
        methodCommentNode.descriptionList.add("描述2")
        methodCommentNode.descriptionList.add("描述3")
        methodCommentNode.descriptionList.add("描述4")
        methodCommentNode.requestUrl = "/api"
        methodCommentNode.requestMethod = "GET,POST"
        methodCommentNode.requestArgList.add(Argument("page", "页码", "是", "Integer"))
        methodCommentNode.requestArgList.add(Argument("pageSize", "分页大小", "否", "Integer"))
//        println(MarkdownTemplate(text, methodCommentNode).render())
    }
}