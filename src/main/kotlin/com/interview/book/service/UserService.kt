package com.interview.book.service

import com.interview.book.domain.BookUser
import com.interview.book.repository.BookUserRepository
import com.interview.book.service.dto.CreateUserDTO
import com.interview.book.service.dto.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(request: CreateUserDTO): UserDTO
}

@Service
class UserServiceImpl(
    val bookUserRepository: BookUserRepository,
    val passwordEncoder: PasswordEncoder
) : UserService {
    override fun createUser(request: CreateUserDTO): UserDTO {
        val saved = bookUserRepository.save(
            BookUser(
                id = null,
                firstName = request.firstName,
                lastName = request.lastName,
                username = request.username,
                password = passwordEncoder.encode(request.password),
                dateOfBirth = request.dateOfBirth
            )
        )
        return UserDTO(saved.firstName, saved.lastName, saved.dateOfBirth)
    }

}