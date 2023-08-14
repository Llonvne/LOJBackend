package cn.llonvne.lojbackend.security

import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface LoginUser {
    val user: User
}

@Service
private class UserDetailServiceImpl(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let {
            val user = userRepository.findUserByUsername(username)
                ?: throw UsernameNotFoundException("username $username not exist")
            return LoginUserDetailsImpl(user)
        } ?: throw UsernameNotFoundException("username is null")
    }
}


private class LoginUserDetailsImpl(override val user: User) : UserDetails, LoginUser {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword() = user.encodedPassword

    override fun getUsername() = user.username

    override fun isEnabled() = user.enabled

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true
}