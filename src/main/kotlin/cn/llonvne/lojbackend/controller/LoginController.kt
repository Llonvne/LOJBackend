package cn.llonvne.lojbackend.controller

import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.service.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    val loginService: LoginService
) {
    @PostMapping("/user/login")
    fun login(@RequestBody user: User) {

    }
}