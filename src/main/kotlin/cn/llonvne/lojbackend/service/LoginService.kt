package cn.llonvne.lojbackend.service

import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.entity.userFromSecurityContextHolder
import cn.llonvne.lojbackend.response.*
import cn.llonvne.lojbackend.security.Jwt
import cn.llonvne.lojbackend.security.AuthenticationUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
interface LoginService {
    suspend fun login(userDto: LoginUserDto): LoginResponse
    suspend fun logout(): Response<String>
}

@Service
private class LoginServiceImpl(
    val authenticationManager: AuthenticationManager,
    val jwt: Jwt,
    val userRedisService: UserRedisService
) : LoginService {

    /**
     * 生成 JWT 响应
     * @param user 用户
     * @return JWT 响应 [LoginResponse]
     */
    private fun userJwtResponse(user: User): LoginResponse {
        val userId = user.id
        val token = jwt.generateToken(userId.toString())
        return LoginSuccessful(token)
    }

    /**
     * 登入
     * @param userDto 用户登入数据传输对象
     * @return 登入结果 [LoginResponse]
     */
    @Transactional
    override suspend fun login(userDto: LoginUserDto): LoginResponse {

        // 从 userDto 中获取用户名和密码，构造 UsernamePasswordAuthenticationToken
        val token = UsernamePasswordAuthenticationToken(userDto.username, userDto.password)

        // 尝试使用 AuthenticationManager 进行认证，检查是否抛出 BadCredentialsException 异常
        // 如果抛出异常，返回 LoginFailure 表示登入失败
        // 否则获得 Authentication 对象
        val authentication = try {
            authenticationManager.authenticate(token)
        } catch (e: BadCredentialsException) {
            return LoginFailure
        }

        // 从 Authentication 对象中获得 LoginUser 对象
        val principal = authentication.principal as AuthenticationUser
        // 从 LoginUser 对象中获得 User 对象
        val user = principal.user

        // 将 User 对象使用 userRedisService 存储到 Redis 中
        userRedisService.store(user)

        // 调用 userJwtResponse 方法，返回 LoginResponse 对象
        return userJwtResponse(user)
    }

    /**
     * 登出
     * @return 登出结果 [Response]
     * @see Response
     */
    override suspend fun logout(): Response<String> {
        // 从 SecurityContextHolder 中获得 User 对象
        val user = userFromSecurityContextHolder()
        // 从 Redis 中删除 User 对象
        userRedisService.delete(user)
        // 返回登出成功的 Response
        return Ok("登出成功")
    }
}