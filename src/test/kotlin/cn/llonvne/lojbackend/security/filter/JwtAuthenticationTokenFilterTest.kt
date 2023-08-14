package cn.llonvne.lojbackend.security.filter

import cn.llonvne.lojbackend.TestLojBackendApplication
import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.repository.UserRepository
import cn.llonvne.lojbackend.service.LoginService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.lang.reflect.Method
import kotlin.reflect.full.declaredFunctions

@TestPropertySource(locations = ["classpath:test.properties"])
@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class JwtAuthenticationTokenFilterTest {
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
        if (userRepository.findUserByUsername("123") == null){
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

    val authentication get() = SecurityContextHolder.getContext().authentication

    @Test
    fun testJwtAuthenticationTokenFilter() {

        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = Mockito.mock(FilterChain::class.java)


        runBlocking {
            val token = loginService.login(LoginUserDto("123", "123")).data?.get("token") as String

            // 尝试登入
            jwtAuthenticationTokenFilter.doFilterInternal(request, response, filterChain)

            assert(authentication == null)

            request.addHeader("token", token)

            jwtAuthenticationTokenFilter.doFilterInternal(request, response, filterChain)

            assert(authentication != null)
            println(authentication)
        }
    }
}