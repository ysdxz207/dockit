# Dockit

## Dockit is a markdown document generator via javadoc.
> ## Dockit 是一个通过javadoc生成markdown文档的生成工具。


2. pom.xml:

```xml
<plugin>
    <groupId>com.hupubao</groupId>
    <artifactId>dockit-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <scanPackage>cn.lamic.chagoi</scanPackage>
        <templateCharset>UTF-8</templateCharset>
        <singleOutDir>false</singleOutDir>
        <outDir>${project.build.directory}/dockit</outDir>
        <template>${basedir}/src/main/resources/dockit/TEMPLATE.MD</template>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>dockit</goal>
            </goals>
        </execution>
    </executions>
</plugin>

```

3. `mvn clean dockit:dockit`

4. You can custom your doc template with [DEFAULT.MD](https://github.com/ysdxz207/dockit/blob/master/src/main/resources/template/DEFAULT.MD)

> 你也可以根据默认模版 [DEFAULT.MD](https://github.com/ysdxz207/dockit/blob/master/src/main/resources/template/DEFAULT.MD)自定义你的文档模版

5. javadoc format:

> javadoc 格式如下：

```
/**
 * @title 阿福列表接口
 * @desc 获取阿福列表
 * @url afu.list
 * @version 1.0.0
 * @status 可用
 * @method POST
 * @arg pageNum,Integer,是 页码
 * @arg pageSize,Integer,否 分页大小
 * @resArg service,String,是 请求服务，原样返回
 * @resArg afuType.id,String,是 阿福类型ID
 * @resArg afuType.name,String,是 阿福类型名
 * @resArg list,Array,是 阿福列表
 * @resArg list[id],String,是 ID
 * @resArg list[name],String,是 名称
 * @resArg list[type],String,是 类型
 * @resArg list[createTime],String,是 创建时间
 * @resArg list[content],String,是 内容
 */
 
 ```
 
 - 返回值也可以用`@return {@link win.hupubao.bean.AfuBean}`，但这与@resArg不能共用