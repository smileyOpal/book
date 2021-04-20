package com.interview.book.domain

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "book_order")
data class BookOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(name = "user_id", nullable = false) val userId: Long,
) : AbstractAuditingEntity(), Serializable
