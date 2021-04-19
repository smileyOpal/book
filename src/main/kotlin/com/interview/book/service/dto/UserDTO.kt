package com.interview.book.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class UserDTO(
    @JsonProperty("name") val firstName: String,
    @JsonProperty("surname") val lastName: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date? = null
): Serializable

data class UserOrderDTO(
    @JsonProperty("name") val firstName: String,
    @JsonProperty("surname") val lastName: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date? = null,
    val books: List<Long>
): Serializable

data class CreateUserDTO(
    @NotBlank @JsonProperty("name") val firstName: String,
    @NotBlank @JsonProperty("surname") val lastName: String,
    @NotBlank val username: String,
    @NotBlank val password: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date?
)

data class CreateOrderDTO(@NotEmpty(message = "Must not empty") val orders: List<Long>)
data class CalculatedOrderDTO(val price: Double)
