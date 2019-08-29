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
    <version>2.1.2.RELEASE</version>
    <configuration>
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

3. 你也可以根据默认模版 [DEFAULT.MD](https://github.com/ysdxz207/dockit/blob/master/src/main/resources/template/DEFAULT.MD)
自定义你的文档模版，将插件template参数修改为你的模板路径即可

4. javadoc 格式如下：

```
/**
 * @title 阿福列表接口
 * @desc 获取阿福列表
 * @url afu.list
 * @version 1.0.0
 * @status 可用
 * @method POST
 * @arg pageNum,Integer,是,页码
 * @arg pageSize,Integer,否,分页大小
 * @arg obj, Object, 否, 参数
 * @arg obj.name, String, 否, 名称
 * @arg obj.createTime, String, 否, 创建事件
 * @resArg service,Object,是,请求服务，原样返回
 * @resArg service.subObject,Object,是,service子对象
 * @resArg service.subObject.id,Integer,是,service子对象ID
 * @resArg service.subObject.name,String,是,service子对象名
 * @resArg service.subArray,Array,是,service子数组
 * @resArg service.subArray.name,String,是,service子数组名
 * @resArg list,Array,是,阿福列表
 * @resArg list.id,String,是,ID
 * @resArg list.name,String,是,名称
 * @resArg list.type,String,是,类型
 * @resArg list.createTime,String,是,创建时间
 * @resArg list.content,String,是,内容
 * @resArg list.arr,Array,是,数组
 * @resArg list.arr.arrName,String,是,数组名
 * @resArg list.arr.arrValue,String,是,数组值
 * @resArg list.arr.subArr,Array,是,子数组
 * @resArg list.arr.subArr.subArrId,String,是,子数组ID
 * @resArg list.arr.subArr.subArrName,String,是,子数组名
 * @resArg list.arr.subObj,Object,是,子对象
 * @resArg list.arr.subObj.objId,String,是,子对象ID
 * @resArg list.arr.subObj.objName,String,是,子对象名
 *
 */
 
 ```
 
 - 生成[样例](./example/afu.md)
 
 - 标签释义
 
 |标签名|释义|格式|
 |:----    |:---|-----   |
 |`@title` |接口名，若需要此接口生成文档，则此标签必选| - |
 |`@arg` |请求参数| `参数名,类型,是否必填,参数描述`|
 |`@resArg` |返回参数| `参数名[.属性],类型(除其他类型外还可以是Object或Array),是否必填,参数描述`|
 
 
 
 **历史更新**
 
 - **2019-08-29因更换域名，更换了插件的groupId从win.hupubao改为com.hupubao**
 
 - **2.1.1.RELEASE**
 
 1. 解决字段描述中出现英文逗号则会丢失逗号后部分内容问题
 
 
 - **2.1.0.RELEASE**
 
 1. 请求参数也可以嵌套对象了
 
 2. 增加请求参数示例
 
 