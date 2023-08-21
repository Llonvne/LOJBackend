package cn.llonvne.lojbackend.security

import cn.llonvne.lojbackend.security.filter.JwtAuthenticationTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    val jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter
) {
//    @Bean
//    fun corsConfigurer(): WebMvcConfigurer {
//        return object : WebMvcConfigurer {
//            override fun addCorsMappings(registry: CorsRegistry) {
//                registry.addMapping("/graphql") // 你的GraphQL端点
//                    .allowedOrigins("*") // 允许的域名
//                    .allowedMethods("GET", "POST") // 允许的HTTP方法
//
//                registry.addMapping("/login")
//                    .allowedMethods("*")
//            }
//        }
//    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun configure(http: HttpSecurity): DefaultSecurityFilterChain? {
        http {
            csrf { disable() }

            // 启用无状态
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            // 配置拦截
            authorizeRequests {
                authorize("/login", permitAll)

                // for hello
                authorize("/hello", permitAll)

                authorize(anyRequest, authenticated)
            }


            // 关闭 Spring Security 提供的默认登入配置
            this.logout {
                disable()
            }
            this.formLogin {
                disable()
            }
            this.httpBasic {
                disable()
            }
            // 添加过滤器
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationTokenFilter)
        }
        return http.build()
    }
}