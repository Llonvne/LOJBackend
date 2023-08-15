package cn.llonvne.lojbackend.entity.types

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

class Authority(private val authority: String) : GrantedAuthority {
    override fun getAuthority() = authority
}

fun authorityOf(authority: String) = Authority(authority)

@Component
class AuthorityConverter(private val objectMapper: ObjectMapper) : RedisSerializer<Authority> {
    override fun serialize(t: Authority?): ByteArray {
        return objectMapper.writeValueAsBytes(t?.authority)
    }

    override fun deserialize(bytes: ByteArray?): Authority {
        return authorityOf(
            objectMapper.readValue(bytes, String::class.java)
        )
    }
}