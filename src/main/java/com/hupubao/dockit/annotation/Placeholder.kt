package com.hupubao.dockit.annotation

import com.hupubao.dockit.enums.PlaceholderType
import java.lang.annotation.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Documented
@Inherited// 支持继承，这样在aop的cglib动态代理类中才能获取到注解
annotation class Placeholder(val value: String = "", val type: PlaceholderType = PlaceholderType.SIMPLE)
