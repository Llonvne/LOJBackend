package cn.llonvne.lojbackend.service

import cn.llonvne.lojbackend.entity.userOf
import cn.llonvne.lojbackend.repository.UserRepository
import cn.llonvne.lojbackend.security.Jwt
import cn.llonvne.lojbackend.test.LOJTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

@LOJTest
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