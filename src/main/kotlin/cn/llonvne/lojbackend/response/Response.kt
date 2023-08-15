package cn.llonvne.lojbackend.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus


@JsonInclude(JsonInclude.Include.NON_NULL)
open class Response<T>(
    open val code: Int,
    open val msg: String? = null,
    open val data: T? = null
) {
    constructor(
        code: HttpStatus,
        msg: String? = null,
        data: T? = null
    ) : this(code.value(), msg, data)
}


fun <Type> Ok(msg: String? = null, data: Type? = null) = Response(HttpStatus.OK, msg, data)