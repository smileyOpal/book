package com.interview.book.domain

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "book")
data class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
    var price: Double = 0.0,
    @Column(name = "book_name", length = 255, nullable = false)
    val bookName: String,
    @Column(name = "author_name", length = 255, nullable = false)
    val authorName: String,
    @Column(name = "is_recommended")
    val isRecommended: Boolean = false
) : AbstractAuditingEntity(), Serializable