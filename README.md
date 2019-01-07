# Dockit

## Dockit is a markdown document generator via javadoc.
> ## Dockit 是一个通过javadoc生成markdown文档的生成工具。

1. `mvn clean install`

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
