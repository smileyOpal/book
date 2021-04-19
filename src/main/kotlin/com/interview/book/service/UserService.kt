package com.interview.book.service

import com.interview.book.domain.BookUser
import com.interview.book.repository.BookUserRepository
import com.interview.book.rest.error.NotFoundException
import com.interview.book.service.dto.CreateUserDTO
import com.interview.book.service.dto.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(request: CreateUserDTO): UserDTO
    fun getUser(username: String): UserDTO
    fun deleteUser(username: String)
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

    override fun getUser(username: String): UserDTO {
        return bookUserRepository.findOneByUsername(username)
            .map { UserDTO(it.firstName, it.lastName, it.dateOfBirth) }
            .orElseThrow { NotFoundException("User not found") }
    }

    override fun deleteUser(username: String) {
        bookUserRepository.deleteByUsername(username)
    }

}