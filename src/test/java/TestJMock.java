import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import org.junit.Test;

public class TestJMock {

    @Test
    public void test() {
        MockConfig mockConfig = new MockConfig()
                // 全局配置
                .globalConfig()
                .sizeRange(1,10)
                .stringSeed("成功", "失败", "测试")

                // 全局配置
                .globalConfig();
        String str = JMockData.mock(String.class, mockConfig);
        System.out.println(str);
    }
}
