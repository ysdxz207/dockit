# Dockit

> ## Dockit 是一个通过javadoc生成markdown文档的生成工具。

- 不依赖于@RequestMapping注解，直接根据javadoc中是否有@title来生成接口文档

- 可应用于dubbo项目中

- 若需要dockit生成文档，则需要在类的javadoc中加入@dockit
如：
```
/**
 * @author ysdxz207
 * @date 2018-08-02
 * @dockit 阿福接口
 */
```

- 生成dockit目录结构如下：

> dockit - 阿福接口 - 阿福列表接口.MD

- 若不需要按目录生成，则可以设置插件参数singleOutDir=true


1. pom.xml:

```xml
<plugin>
    <groupId>com.hupubao</groupId>
    <artifactId>dockit-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <scanPackage>com.example.your.package.to.scan</scanPackage>
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

2. `mvn clean dockit:dockit`

3. 你也可以根据默认模版 [DEFAULT.MD](https://github.com/ysdxz207/dockit/blob/master/src/main/resources/template/DEFAULT.MD)自定义你的文档模版

4. javadoc 格式如下：

```
/**
 * @title 阿福列表接口
 * @desc 获取阿福列表
 * @desc 可以有多条描述
 * @url afu.list
 * @version 1.0.0
 * @status 可用
 * @method POST,GET
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