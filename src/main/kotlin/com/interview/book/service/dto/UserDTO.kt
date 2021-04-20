package com.interview.book.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.ZonedDateTime
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
    val orders: List<OrderDTO>
): Serializable

data class OrderDTO(
    @JsonProperty("order_id") val orderId: Long,
    @JsonProperty("created_date") val createDate: ZonedDateTime,
    val detail: List<OrderDetailDTO>): Serializable

data class OrderDetailDTO(
    @JsonProperty("book_id") val bookId: Long,
    val amount: Int,
    @JsonProperty("unit_price") val unitPrice: Double,
    @JsonProperty("total_price") val totalPrice: Double): Serializable

data class CreateUserDTO(
    @NotBlank @JsonProperty("name") val firstName: String,
    @NotBlank @JsonProperty("surname") val lastName: String,
    @NotBlank val username: String,
    @NotBlank val password: String,
    @JsonProperty("date_of_birth") val dateOfBirth: Date?
)

data class CreateOrderDTO(@NotEmpty(message = "Must not empty") val orders: List<CreateOrderDetailDTO>)
data class CreateOrderDetailDTO(val bookId: Long, val amount: Int)
data class CalculatedOrderDTO(val price: Double)
