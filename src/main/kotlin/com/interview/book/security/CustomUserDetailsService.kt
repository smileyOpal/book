package com.interview.book.security

import com.interview.book.repository.BookUserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component("userDetailsService")
class CustomUserDetailsService(val bookUserRepository: BookUserRepository) : UserDetailsService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun loadUserByUsername(login: String): UserDetails {
        log.debug("Authenticating $login")
        return bookUserRepository.findOneByUsername(login)
            .map { User(it.username, it.password, emptyList()) }
            .orElseThrow { UsernameNotFoundException("User $login was not found in the database") }
    }

}