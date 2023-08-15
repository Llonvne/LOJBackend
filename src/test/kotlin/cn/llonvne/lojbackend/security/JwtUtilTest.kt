package cn.llonvne.lojbackend.security

import cn.llonvne.lojbackend.test.LOJTest
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@LOJTest
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