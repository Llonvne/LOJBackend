package cn.llonvne.lojbackend.controller

import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.service.LoginService
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(
    origins = ["http://localhost:3000"],
    allowCredentials = "true",
    methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH]
)
internal class LoginController(
    val loginService: LoginService
) {
    @PostMapping("/login")
    suspend fun login(@RequestBody user: LoginUserDto) = loginService.login(user)

    @GetMapping("/logout")
    fun logout() = runBlocking {
        loginService.logout()
    }

    @GetMapping("/hello")
    suspend fun hello(): String {
        return """Hello from LOJ ^-^ 
            |Practice make perfect
        """.trimMargin()
    }
}