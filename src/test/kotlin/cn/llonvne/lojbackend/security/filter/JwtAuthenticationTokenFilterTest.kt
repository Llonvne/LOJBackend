package cn.llonvne.lojbackend.security.filter

import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.repository.UserRepository
import cn.llonvne.lojbackend.response.LoginFailure
import cn.llonvne.lojbackend.response.LoginSuccessful
import cn.llonvne.lojbackend.security.Jwt
import cn.llonvne.lojbackend.service.LoginService
import cn.llonvne.lojbackend.test.LOJTest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.lang.reflect.Method
import kotlin.jvm.optionals.getOrNull

@LOJTest
class JwtAuthenticationTokenFilterTest {
    @Autowired
    private lateinit var jwt: Jwt

    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter

    @BeforeEach
    fun addUser() {
        if (userRepository.findUserByUsername("123") == null) {
            userRepository.save(
                userOf(
                    "123",
                    password = passwordEncoder.encode("123").toString()
                )
            )
        }
    }

    private val method: Method =
        JwtAuthenticationTokenFilter::class.java.declaredMethods.filter { it.name == "doFilterInternal" }[0]

    private fun JwtAuthenticationTokenFilter.doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        method.invoke(this, httpServletRequest, httpServletResponse, filterChain)
    }

    @Test
    fun testJwtAuthenticationTokenFilter() {

        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = Mockito.mock(FilterChain::class.java)


        runBlocking {
            val loginResp = loginService.login(LoginUserDto("123", "123"))

            val token: String = when (loginResp) {
                LoginFailure -> fail("密码正确，但是登入失败")
                is LoginSuccessful -> loginResp.data ?: ""
                else -> {
                    fail("未知的登入结果")
                }
            }

            // 尝试登入
            jwtAuthenticationTokenFilter.doFilterInternal(request, response, filterChain)

            val userId = jwt.parseToken(token)
            println(userId)
            val username = userRepository.findById(userId.toLong()).getOrNull()?.username
            assert("123" == username)
        }
    }
}