package com.interview.book.security.jwt

import com.interview.book.config.ApplicationProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(applicationProperties: ApplicationProperties) {

    private val AUTHORITIES_KEY = "auth"
    private val log = LoggerFactory.getLogger(this::class.java)

    private val base64Secret = applicationProperties.jwt.base64Secret
    private val keyBytes = Decoders.BASE64.decode(base64Secret)
    private val key = Keys.hmacShaKeyFor(keyBytes)
    private val jwtParser = Jwts.parserBuilder().setSigningKey(key).build()
    private val tokenValidityInMilliseconds = applicationProperties.jwt.tokenValidityInSeconds * 1000
    private val tokenValidityInMillisecondsForRememberMe =
        applicationProperties.jwt.tokenValidityInSecondsForRememberMe * 1000

    fun createToken(authentication: Authentication, rememberMe: Boolean): String {
        val authorities = authentication.authorities.stream().map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        val now = Date().time
        val validity: Date = if (rememberMe) {
            Date(now + tokenValidityInMillisecondsForRememberMe)
        } else {
            Date(now + tokenValidityInMilliseconds)
        }
        return Jwts
            .builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = jwtParser.parseClaimsJws(token).body
        val principal = User(claims.subject, "", emptyList())
        return UsernamePasswordAuthenticationToken(principal, token, emptyList())
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            jwtParser.parseClaimsJws(authToken)
            return true
        } catch (e: JwtException) {
            log.info("Invalid JWT token.")
            log.trace("Invalid JWT token trace.", e)
        } catch (e: IllegalArgumentException) {
            log.info("Invalid JWT token.")
            log.trace("Invalid JWT token trace.", e)
        }
        return false
    }
}