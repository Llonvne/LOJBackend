package cn.llonvne.lojbackend.entity

import cn.llonvne.lojbackend.entity.types.Sex
import cn.llonvne.lojbackend.entity.types.UserType
import cn.llonvne.lojbackend.security.LoginUser
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.security.core.context.SecurityContextHolder

inline fun userOf(username: String, password: String, configuration: User.() -> Unit = {}) = User().apply {
    this.username = username
    this.encodedPassword = password
    configuration()
}

val User.redisKey get() = fromUserId(id.toString())

fun fromUserId(id: String) = "login:$id"

fun userFromSecurityContextHolder(): User? {
    val loginUser = SecurityContextHolder.getContext()?.authentication?.principal as LoginUser?
    return loginUser?.user
}


@Entity
@Table(name = "tb_user")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "username", nullable = false)
    open var username: String = ""

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "encoded_password", nullable = false)
    open var encodedPassword: String? = null

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "email")
    open var email: String? = null

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "avatar_url")
    open var avatarUrl: String? = null

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 20)
    open var sex: Sex = Sex.Unknown

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", length = 20)
    open var userType: UserType = UserType.User

    @JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(name = "enabled")
    open var enabled: Boolean = true

    @JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(name = "deleted")
    open var deleted: Boolean = false
}