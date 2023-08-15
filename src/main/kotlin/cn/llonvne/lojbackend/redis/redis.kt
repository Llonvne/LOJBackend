package cn.llonvne.lojbackend.redis

import cn.llonvne.lojbackend.LojInternalApi
import cn.llonvne.lojbackend.entity.types.AuthorityConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import java.time.Duration

interface Redis {
    suspend fun set(key: String, value: Any, timeout: Duration? = null): Boolean
    suspend fun get(key: String): Any?
    suspend fun delete(key: String): Long
    suspend fun hasKey(key: String): Boolean

}

@LojInternalApi
suspend inline fun <reified Type> Redis.getTyped(key: String): Type? {
    return jacksonObjectMapper().convertValue(get(key), Type::class.java)
}

@Component
private class CoroutineRedisUtil(private val reactiveRedisOps: ReactiveRedisOperations<String, Any>) : Redis {
    override suspend fun set(key: String, value: Any, timeout: Duration?): Boolean {
        return if (timeout != null) {
            reactiveRedisOps.opsForValue().set(key, value, timeout).awaitSingle()
        } else {
            reactiveRedisOps.opsForValue().set(key, value).awaitSingle()
        }
    }

    override suspend fun get(key: String): Any? {
        return reactiveRedisOps.opsForValue()[key].awaitFirstOrNull()
    }

    override suspend fun delete(key: String): Long {
        return reactiveRedisOps.delete(key).awaitSingle()
    }

    override suspend fun hasKey(key: String): Boolean {
        return reactiveRedisOps.hasKey(key).awaitSingle()
    }
}

@Configuration
private class RedisConfig(private val authorityConverter: AuthorityConverter) {
    @Bean
    fun reactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Any> {
        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
        val builder = RedisSerializationContext
            .newSerializationContext<String, Any>(StringRedisSerializer())
        val context = builder.value(serializer)
            .build()
        return ReactiveRedisTemplate(
            factory, context
        )
    }
}

