package cn.llonvne.lojbackend.dto

/**
 * DTO for [cn.llonvne.lojbackend.entity.User]
 * 用户登入数据传输对象 [cn.llonvne.lojbackend.controller.LoginController.login]
 * @property username 用户名
 * @property password 密码
 */
data class LoginUserDto(val username: String, val password: String)