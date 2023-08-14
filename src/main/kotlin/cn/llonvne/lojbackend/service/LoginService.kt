package cn.llonvne.lojbackend.service

import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.entity.userFromSecurityContextHolder
import cn.llonvne.lojbackend.entity.redisKey
import cn.llonvne.lojbackend.redis.Redis
import cn.llonvne.lojbackend.response.Response
import cn.llonvne.lojbackend.security.Jwt
import cn.llonvne.lojbackend.security.LoginUser
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
interface LoginService {
    suspend fun login(userDto: LoginUserDto): Response<Map<String, String>>
    suspend fun logout(): Response<String>
}

@Service
private class LoginServiceImpl(
    val authenticationManager: AuthenticationManager,
    val jwt: Jwt,
    val redis: Redis
) : LoginService {

    fun userJwtResponse(user: User): Response<Map<String, String>> {
        val userId = user.id?.toString()
        if (userId == null) {

        } else {
            return Response(
                HttpStatus.OK,
                "登入成功",
                mapOf(
                    "token" to jwt.generateToken(userId),
                )
            )
        }
        throw NotImplementedError()
    }

    override suspend fun login(userDto: LoginUserDto): Response<Map<String, String>> {
        val token = UsernamePasswordAuthenticationToken(userDto.username, userDto.password)
        try {
            val authentication = authenticationManager.authenticate(token)
            val principal = authentication.principal as LoginUser
            val user = principal.user
            redis.set(user.redisKey, user)
            return userJwtResponse(user)
        } catch (e: BadCredentialsException) {

        }
        throw NotImplementedError()
    }

    override suspend fun logout(): Response<String> {
        val user = userFromSecurityContextHolder()
        if (user != null) {
            redis.delete(user.redisKey)
        }
        return Response(HttpStatus.OK, "登出成功")
    }
}