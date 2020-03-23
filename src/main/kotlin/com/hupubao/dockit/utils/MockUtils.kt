package com.hupubao.dockit.utils

import com.github.jsonzou.jmockdata.JMockData
import com.github.jsonzou.jmockdata.MockConfig

object MockUtils {

    private val ENGLISH_SEED = arrayOf("do ", "you ", "like ", "dockit ", "yes ", "I ", "think ", "very ", "much ", "it ", "want ", "have ", "one ", "two ")
    private val CHINESE_SEED = arrayOf("我", "非常", "你", "很", "帅", "可爱", "漂亮", "美", "吗", "对吧")


    public fun mockFromEnglishSeed(): String {
        val config = MockConfig().sizeRange(3, 5)
            .stringSeed(*ENGLISH_SEED)
        return JMockData.mock(String::class.java, config).trim()
    }

    public fun mockFromChineseSeed(): String {
        val config = MockConfig().sizeRange(2, 4)
            .stringSeed(*CHINESE_SEED)
        return JMockData.mock(String::class.java, config)
    }
}