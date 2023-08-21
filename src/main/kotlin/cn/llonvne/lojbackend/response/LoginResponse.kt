package cn.llonvne.lojbackend.response

import org.springframework.http.HttpStatus

data object LoginFailure : Response<Unit>(
    HttpStatus.UNAUTHORIZED,
    "用户名或密码错误",
    null
)


class LoginSuccessful(token: String) : Response<String>(
    HttpStatus.OK,
    "登入成功",
    token
)

