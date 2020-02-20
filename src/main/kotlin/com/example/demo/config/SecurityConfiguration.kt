package com.example.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {

    @Bean fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .authorizeExchange {
            it.pathMatchers("/index.html", "/", "/home", "login").permitAll().anyExchange().authenticated()
        }
        .configureFormLogin()
        .configureExceptionHandling()
        .logout { it.logoutSuccessHandler(HttpStatusReturningServerLogoutSuccessHandler()) }
        .csrf {
            // csrf can be disabled, since the session cookie has SameSite=Lax
            it.disable()
        }
        .build()

    private fun ServerHttpSecurity.configureFormLogin() = formLogin {
        it.loginPage("/login")
            .authenticationFailureHandler { exchange, exception -> handleAuthError(exchange.exchange, exception) }
            .authenticationSuccessHandler { _, _ -> Mono.empty() }
    }

    private fun ServerHttpSecurity.configureExceptionHandling() = exceptionHandling {
        it.authenticationEntryPoint(::handleAuthError)
            .accessDeniedHandler(::handleAuthError)
    }

    private fun handleAuthError(exchange: ServerWebExchange, exception: Exception): Mono<Void> {
        val status = when (exception) {
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            else -> HttpStatus.UNAUTHORIZED
        }
        exchange.response.statusCode = status
        return Mono.empty()
    }

    @Bean fun userDetailsRepository(): MapReactiveUserDetailsService {
        val users = withDefaultPasswordEncoder()
        val user = users.username("user").password("password").roles("USER").build()
        val admin = users.username("admin").password("password").roles("USER", "ADMIN").build()
        return MapReactiveUserDetailsService(user, admin)
    }
}
