package cn.llonvne.lojbackend.redis

import cn.llonvne.lojbackend.TestLojBackendApplication
import cn.llonvne.lojbackend.entity.userOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@TestPropertySource(locations = ["classpath:test.properties"])
class CoroutineRedisUtilTest {
    @Autowired
    private lateinit var redis: Redis

    @Test
    fun basic() {
        runBlocking {
            redis.set("123", userOf("123", "123"))
            println(redis.get("123"))
        }
    }
}