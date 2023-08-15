package cn.llonvne.lojbackend.controller

import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.response.Ok
import cn.llonvne.lojbackend.response.Response
import cn.llonvne.lojbackend.service.LoginService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
internal class LoginController(
    val loginService: LoginService
) {
    @PostMapping("/login")
    suspend fun login(@RequestBody user: LoginUserDto) = loginService.login(user)

    @GetMapping("/logout")
    suspend fun logout(): Response<String> {
        return loginService.logout()
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('user')")
    suspend fun test(): Response<String> {
        return Ok("Hello from LOJ ^-^")
    }
}