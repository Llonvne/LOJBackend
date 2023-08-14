package cn.llonvne.lojbackend

import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.Logger
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.with
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import kotlin.math.log

@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@TestPropertySource(locations = ["classpath:test.properties"])
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