package com.interview.book.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Stream

class SecurityUtils {

    companion object {
        fun getCurrentUserLogin(): Optional<String> {
            val securityContext = SecurityContextHolder.getContext()
            return Optional.ofNullable(extractPrincipal(securityContext.authentication))
        }

        private fun extractPrincipal(authentication: Authentication?): String? {
            if (authentication == null) {
                return null
            }
            else if (authentication.principal is UserDetails) {
                val springSecurityUser = authentication.principal as UserDetails
                return springSecurityUser.username
            }
            else if (authentication.principal is String) {
                return authentication.principal as String
            }
            else return null
        }

        fun getCurrentUserJWT(): Optional<String> {
            val securityContext = SecurityContextHolder.getContext()
            return Optional
                .ofNullable(securityContext.authentication)
                .filter { authentication: Authentication -> authentication.credentials is String }
                .map { authentication: Authentication -> authentication.credentials as String }
        }

        fun isAuthenticated(): Boolean = SecurityContextHolder.getContext().authentication != null

        private fun getAuthorities(authentication: Authentication): Stream<String?> {
            return authentication.authorities.stream().map { obj: GrantedAuthority -> obj.authority }
        }
    }
}