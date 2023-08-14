package cn.llonvne.lojbackend.security.filter

import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.entity.fromUserId
import cn.llonvne.lojbackend.redis.Redis
import cn.llonvne.lojbackend.redis.getTyped
import cn.llonvne.lojbackend.security.Jwt
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationTokenFilter(
    private val jwt: Jwt,
    val redis: Redis
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("token")

        // 没有 token 放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response)
        }

        val userId = try {
            jwt.parseToken(token)
        } catch (e: Exception) {
            // 解析失败，表示 token 无效直接放行
            filterChain.doFilter(request, response)
            return
        }

        val user = runBlocking { redis.getTyped<User>(fromUserId(userId)) }

        // 无法获取用户
        if (user == null) {
            filterChain.doFilter(request, response)
            return
        }

        // 存入 ContextHolder
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                user.username,
                user.encodedPassword,
                // TODO 需要补充用户的权限信息
                listOf()
            )

        filterChain.doFilter(request, response)
    }
}