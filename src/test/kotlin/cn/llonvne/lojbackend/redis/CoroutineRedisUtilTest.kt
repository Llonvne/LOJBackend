package cn.llonvne.lojbackend.redis

import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.test.LOJTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@LOJTest
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