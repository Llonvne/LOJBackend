package cn.llonvne.lojbackend.response

import cn.llonvne.lojbackend.json.json
import cn.llonvne.lojbackend.json.jsonValue
import cn.llonvne.lojbackend.test.LOJTest
import org.junit.jupiter.api.Test

@LOJTest
class LoginSuccessfulTest {
    @Test
    fun testSerializer() {
        println(json { LoginSuccessful("123").jsonValue })
    }
}