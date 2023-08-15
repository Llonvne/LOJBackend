package cn.llonvne.lojbackend.entity

import cn.llonvne.lojbackend.LojInternalApi
import cn.llonvne.lojbackend.security.AuthenticationUser
import org.springframework.security.core.context.SecurityContextHolder

/**
 * userOf 函数用于快速创建用户对象
 * @param username 用户名
 * @param password 密码(此处密码将直接写入数据库)，请确保密码已经经过加密
 * @param encode 是否需要对密码进行加密,默认是 false
 * @param configuration 配置User函数
 */
@LojInternalApi
inline fun userOf(
    username: String, password: String,
    encode: Boolean = false,
    configuration: User.() -> Unit = {}
) = User().apply {
    this.username = username
    this.encodedPassword = password
}.apply(configuration)

fun userFromSecurityContextHolder(): User? {
    val authenticationUser = SecurityContextHolder.getContext()?.authentication?.principal as AuthenticationUser?
    return authenticationUser?.user
}