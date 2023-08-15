package cn.llonvne.lojbackend

import cn.llonvne.lojbackend.test.LOJTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

@LOJTest
class LojBackendApplicationTests {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun testPasswordEncoder() = with(passwordEncoder) {
        val origin = "123"
        val encoded = encode(origin)
        println(encoded)
    }


    @Test
    fun contextLoads() {
    }
}