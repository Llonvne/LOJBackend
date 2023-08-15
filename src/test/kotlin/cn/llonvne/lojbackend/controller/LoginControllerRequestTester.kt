package cn.llonvne.lojbackend.controller

import cn.llonvne.lojbackend.API
import cn.llonvne.lojbackend.asyncTo
import cn.llonvne.lojbackend.dto.LoginUserDto
import cn.llonvne.lojbackend.json.json
import cn.llonvne.lojbackend.json.jsonValue
import cn.llonvne.lojbackend.jsonContent
import cn.llonvne.lojbackend.postJson
import cn.llonvne.lojbackend.response.LoginResponse
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc

@Component
class LoginControllerRequestTester(private val mockMvc: MockMvc) {

    suspend fun login(userDto: LoginUserDto): LoginResponse {
        return mockMvc.postJson(API.LoginModule.LOGIN_API) {
            jsonContent(userDto)
            content = json { userDto.jsonValue }
        }.asyncTo()
    }
}