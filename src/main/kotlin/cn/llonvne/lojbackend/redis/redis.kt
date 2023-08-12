package cn.llonvne.lojbackend.redis

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
private class RedisConfig {
    @Bean
    fun reactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Any> {
        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
        return ReactiveRedisTemplate(
            factory, RedisSerializationContext.newSerializationContext<String, Any>(
                StringRedisSerializer()
            ).value(serializer).build()
        )
    }
}

