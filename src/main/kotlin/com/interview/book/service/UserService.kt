package com.interview.book.service

import com.interview.book.domain.BookUser
import com.interview.book.repository.BookUserRepository
import com.interview.book.rest.error.ForbiddenException
import com.interview.book.rest.error.NotFoundException
import com.interview.book.security.SecurityUtils
import com.interview.book.service.dto.CreateUserDTO
import com.interview.book.service.dto.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(request: CreateUserDTO): UserDTO
    fun getUser(): UserDTO
    fun getUserEntity(): BookUser
    fun deleteUser()
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

    override fun getUser(): UserDTO {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        val userDTO = bookUserRepository.findOneByUsername(username)
            .map { UserDTO(it.firstName, it.lastName, it.dateOfBirth) }
            .orElseThrow { NotFoundException("User not found") }
        return userDTO
    }

    override fun getUserEntity(): BookUser {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        return bookUserRepository.findOneByUsername(username)
            .orElseThrow { NotFoundException("User not found") }
    }

    override fun deleteUser() {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        bookUserRepository.deleteByUsername(username)
    }

}