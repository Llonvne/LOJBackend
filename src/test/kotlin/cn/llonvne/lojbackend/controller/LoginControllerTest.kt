package cn.llonvne.lojbackend.controller

import cn.llonvne.lojbackend.API
import cn.llonvne.lojbackend.asyncTo
import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.types.authorityOf
import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.json.json
import cn.llonvne.lojbackend.json.jsonValue
import cn.llonvne.lojbackend.jsonContent
import cn.llonvne.lojbackend.postJson
import cn.llonvne.lojbackend.repository.UserRepository
import cn.llonvne.lojbackend.response.LoginFailure
import cn.llonvne.lojbackend.response.LoginSuccessful
import cn.llonvne.lojbackend.test.LOJTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@LOJTest
@AutoConfigureMockMvc
@EnableAsync
class LoginControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var loginControllerRequestTester: LoginControllerRequestTester

    @BeforeEach
    fun addUser() {
        if (userRepository.findUserByUsername("123") == null) {
            userRepository.save(
                userOf(
                    "123",
                    password = passwordEncoder.encode("123").toString()
                ) {
                    authorities = mutableListOf(
                        authorityOf("user")
                    )
                }
            )
        }
    }

    @Test
    fun testLogin() {
        runBlocking {
            addUser()

            assertDoesNotThrow {
                val response = loginControllerRequestTester.login(LoginUserDto("123", "123"))
                assert(response is LoginSuccessful)
            }

            assertDoesNotThrow {
                val response = loginControllerRequestTester.login(LoginUserDto("123", "1234"))
                assert(response is LoginFailure)
            }
        }
    }

    @Test
    fun testAuthorities() {
        mockMvc.get("/hello")
            .andExpect {
                status { is4xxClientError() }
            }

        val response = mockMvc.postJson(API.LoginModule.LOGIN_API) {
            jsonContent(LoginUserDto("123", "123"))
            content = json { LoginUserDto("123", "123").jsonValue }
        }.asyncTo<LoginSuccessful>()

        mockMvc.get("/hello") {
            header("token", response.token)
        }.andExpect {
            status { isOk() }
        }
    }
}