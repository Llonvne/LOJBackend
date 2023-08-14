package cn.llonvne.lojbackend.repository

import cn.llonvne.lojbackend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    fun findUserByUsername(username: String): User?
}