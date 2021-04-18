package com.interview.book.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class BookDTO(
    val id: Long,
    val name: String,
    val price: Double = 0.0,
    val author: String,
    @JsonProperty("is_recommended") val isRecommended: Boolean = false
): Serializable
