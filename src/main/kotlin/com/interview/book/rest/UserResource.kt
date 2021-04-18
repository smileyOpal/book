package com.interview.book.rest

import com.interview.book.service.UserService
import com.interview.book.service.dto.CreateUserDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * A Resource/Controller layer that defined request interfaces and receives requests
 */
@RestController
@RequestMapping("/api")
@Api("User resource")
class UserResource(val userService: UserService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/users")
    @ApiOperation(notes = "Create an user", value = "Create user")
    fun createUser(@RequestBody request: CreateUserDTO): ResponseEntity<Void> {
        log.info("Request create user with request $request")
        userService.createUser(request)
        return ResponseEntity.ok().build()
    }
}