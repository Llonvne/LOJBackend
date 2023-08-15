package cn.llonvne.lojbackend.security

import cn.llonvne.lojbackend.entity.User
import cn.llonvne.lojbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface AuthenticationUser {
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
            return AuthenticationUserDetailsImpl(user)
        } ?: throw UsernameNotFoundException("username is null")
    }
}


private class AuthenticationUserDetailsImpl(override val user: User) : UserDetails, AuthenticationUser {
    override fun getAuthorities() = user.authorities

    override fun getPassword() = user.encodedPassword

    override fun getUsername() = user.username

    override fun isEnabled() = user.enabled

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true
}
