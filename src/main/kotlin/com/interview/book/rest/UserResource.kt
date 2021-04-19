package com.interview.book.rest

import com.interview.book.rest.error.ForbiddenException
import com.interview.book.security.SecurityUtils
import com.interview.book.service.BookService
import com.interview.book.service.UserService
import com.interview.book.service.dto.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * A Resource/Controller layer that defined request interfaces and receives requests
 */
@RestController
@RequestMapping("/api")
@Api("User resource")
class UserResource(val userService: UserService, val bookService: BookService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/users")
    @ApiOperation(notes = "Create an user", value = "Create user")
    fun createUser(@RequestBody request: CreateUserDTO): ResponseEntity<Void> {
        log.info("Request create user with request $request")
        userService.createUser(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/users")
    @ApiOperation(notes = "Get an user", value = "Get user")
    fun getUser(): ResponseEntity<UserOrderDTO> {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        val user = userService.getUser(username)
        return ResponseEntity.ok(UserOrderDTO(user.firstName, user.lastName, user.dateOfBirth, emptyList()))
    }

    @DeleteMapping("/users")
    @ApiOperation(notes = "Delete an user", value = "Delete user")
    fun deleteUser(): ResponseEntity<Void> {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        userService.deleteUser(username)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/users/orders")
    @ApiOperation(notes = "Order books for an user", value = "Order books")
    fun orderBooks(@RequestBody request: CreateOrderDTO): ResponseEntity<CalculatedOrderDTO> {
        val username: String = SecurityUtils.getCurrentUserLogin().orElseThrow { ForbiddenException("Please login first") }
        return ResponseEntity.ok(bookService.createOrder(username, request))
    }
}