package cn.llonvne.lojbackend.security

import cn.llonvne.lojbackend.TestLojBackendApplication
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class JwtUtilTest {

    @Autowired
    private lateinit var jwt: Jwt

    @Test
    fun generateToken() {
        val token = jwt.generateToken("123")
        jwt.validateToken(token, "123")
        println(jwt.parseToken(token))
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun genKey() {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        val encodedKey = Base64.encode(key.encoded)
        println("Encoded Key: $encodedKey")
    }
}