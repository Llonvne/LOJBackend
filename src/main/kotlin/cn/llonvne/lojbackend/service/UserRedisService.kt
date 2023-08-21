package cn.llonvne.lojbackend.service

import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.redis.Redis
import cn.llonvne.lojbackend.redis.getTyped
import org.springframework.stereotype.Service

private val User.redisKey get() = fromUserId(id.toString())
private fun fromUserId(id: String) = "login:$id"

@Service
interface UserRedisService {
    suspend fun store(user: User)
    suspend fun get(id: String): User?
    suspend fun delete(id: Long?)
}

@Service
private class UserRedisServiceImpl(val redis: Redis) : UserRedisService {
    override suspend fun store(user: User) {
        redis.set(user.redisKey, user)
    }

    override suspend fun get(id: String): User? {
        return redis.getTyped<User>(fromUserId(id))
    }

    override suspend fun delete(id: Long?) {
        if (id != null) {
            redis.delete(fromUserId(id.toString()))
        }
    }
}