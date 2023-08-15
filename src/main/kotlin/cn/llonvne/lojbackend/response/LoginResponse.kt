package cn.llonvne.lojbackend.response

import org.springframework.http.HttpStatus

sealed interface LoginResponse

data object LoginFailure : LoginResponse, Response<Unit>(
    HttpStatus.UNAUTHORIZED,
    "用户名或密码错误",
    null
)


class LoginSuccessful(val token: String) : LoginResponse, Response<Map<String, String>>(
    HttpStatus.OK,
    "登入成功"
)

