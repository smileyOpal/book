package com.interview.book.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.interview.book.security.jwt.JWTFilter
import com.interview.book.security.jwt.TokenProvider
import com.sun.istack.NotNull
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@Api("Login resource")
class LoginResource(
    val authenticationManagerBuilder: AuthenticationManagerBuilder,
    val tokenProvider: TokenProvider
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginDTO): ResponseEntity<JWTToken> {
        val authentication = authenticationManagerBuilder.`object`.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication, request.rememberMe)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }
}

data class LoginDTO(@NotNull val username: String, @NotNull val password: String, val rememberMe: Boolean = false)
data class JWTToken(@JsonProperty("id_token") val idToken: String)