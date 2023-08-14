package cn.llonvne.lojbackend.service

import cn.llonvne.lojbackend.TestLojBackendApplication
import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.entity.userFromSecurityContextHolder
import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.repository.UserRepository
import cn.llonvne.lojbackend.security.Jwt
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@TestPropertySource(locations = ["classpath:test.properties"])
class LoginServiceImplTest {
    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwt: Jwt

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

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

    @Test
    fun test(){

    }
}