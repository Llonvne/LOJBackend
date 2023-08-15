package cn.llonvne.lojbackend.entity

import cn.llonvne.lojbackend.entity.types.Authority
import cn.llonvne.lojbackend.entity.types.Sex
import cn.llonvne.lojbackend.entity.types.UserType
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes


@Entity
@Table(name = "tb_user")
/**
 * 用户实体类
 */
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

    @ElementCollection
    @Convert(converter = AuthorityJpaConverter::class)
    open var authorities: MutableList<Authority> = mutableListOf()
}

@Converter
/**
 * 用于将 Authority 转换为 String
 */
private class AuthorityJpaConverter : AttributeConverter<Authority, String> {
    override fun convertToDatabaseColumn(attribute: Authority): String {
        return attribute.authority
    }

    override fun convertToEntityAttribute(dbData: String?): Authority {
        return Authority(dbData ?: "")
    }
}