package cn.llonvne.lojbackend.security.filter


import cn.llonvne.lojbackend.security.Jwt
import cn.llonvne.lojbackend.service.UserRedisService
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
    private val userRedisService: UserRedisService,
) : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("token")

        // 没有 token 放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val userId = try {
            jwt.parseToken(token)
        } catch (e: Exception) {
            // 解析失败，表示 token 无效直接放行
            filterChain.doFilter(request, response)
            return
        }

        val user = runBlocking { userRedisService.get(userId) }

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
                user.authorities
            )

        filterChain.doFilter(request, response)
    }
}