package com.interview.book.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotBlank

data class UserDTO(
    @JsonProperty("name") val firstName: String,
    @JsonProperty("surname") val lastName: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date? = null
): Serializable

data class CreateUserDTO(
    @NotBlank @JsonProperty("name") val firstName: String,
    @NotBlank @JsonProperty("surname") val lastName: String,
    @NotBlank val username: String,
    @NotBlank val password: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date?
)
