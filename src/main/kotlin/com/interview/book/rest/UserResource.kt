package com.interview.book.rest

import com.interview.book.service.OrderService
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
class UserResource(val userService: UserService, val orderService: OrderService) {
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
        return ResponseEntity.ok(orderService.getUserBooks())
    }

    @DeleteMapping("/users")
    @ApiOperation(notes = "Delete an user", value = "Delete user")
    fun deleteUser(): ResponseEntity<Void> {
        userService.deleteUser()
        return ResponseEntity.ok().build()
    }

    @PostMapping("/users/orders")
    @ApiOperation(notes = "Order books for an user", value = "Order books")
    fun orderBooks(@RequestBody request: CreateOrderDTO): ResponseEntity<CalculatedOrderDTO> {
        return ResponseEntity.ok(orderService.createOrder(request))
    }
}