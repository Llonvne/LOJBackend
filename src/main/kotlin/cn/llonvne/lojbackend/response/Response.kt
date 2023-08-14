package cn.llonvne.lojbackend.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(val code: HttpStatus, val msg: String? = null, val data: T? = null)