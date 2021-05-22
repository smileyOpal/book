package com.interview.book.service

import com.interview.book.domain.BookUser
import com.interview.book.repository.BookUserRepository
import com.interview.book.service.dto.CreateUserDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.security.crypto.password.PasswordEncoder


internal class UserServiceImplTest {
    private lateinit var userService: UserService

    private val bookUserRepository = mock(BookUserRepository::class.java)

    private val passwordEncoder = mock(PasswordEncoder::class.java)

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(bookUserRepository, passwordEncoder)
        `when`(passwordEncoder.encode(anyString())).thenAnswer { it.getArgument<String>(0) }
    }

    @Test
    fun `Assert create user case successfully`() {
        val request = CreateUserDTO("firstname", "lastname", "username", "password", null)
        `when`(bookUserRepository.save(any())).thenAnswer { mock ->
            mock.getArgument<BookUser>(0)
        }
        val actual = userService.createUser(request)
        assertThat(actual.firstName).isEqualTo("firstname")
        assertThat(actual.lastName).isEqualTo("lastname")
    }

    @Test
    fun `Assert create user case fail during save`() {
        val request = CreateUserDTO("firstname", "lastname", "username", "password", null)
        `when`(bookUserRepository.save(any())).thenThrow(
            IllegalArgumentException::class.java
        )
        assertThrows<IllegalArgumentException> {
            userService.createUser(request)
        }
    }
}