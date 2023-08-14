package cn.llonvne.lojbackend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

interface Jwt {
    fun getKey(): Key
    fun generateToken(username: String): String
    fun validateToken(token: String, username: String): Boolean
    fun parseToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .body["sub"] as String
    }
}

private const val EXPIRATION_TIME: Long = 3600000 // 1 小时

@Component
/**
 * @property secretKey HMAC SHA-512 密钥以十六进制表示
 */
private class JwtImpl(
    @Value("\${secretKey}")
    private val secretKey: String
) : Jwt {

    private val key by lazy {
        val keyBytes = secretKey.toByteArray()
        SecretKeySpec(keyBytes, 0, keyBytes.size, "HmacSHA256")
    }

    override fun getKey(): Key = key

    override fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    override fun validateToken(token: String, username: String): Boolean {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject == username && !claims.expiration.before(Date())
    }
}

