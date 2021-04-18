package com.interview.book.domain

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_user")
data class BookUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(name = "first_name", length = 255, nullable = false) val firstName: String,
    @Column(name = "last_name", length = 255, nullable = false) val lastName: String,
    @Column(name = "username", length = 50, nullable = false) val username: String,
    @Column(name = "password_hash", length = 60, nullable = false) val password: String,
    @Column(name = "date_of_birth") @Temporal(TemporalType.DATE) val dateOfBirth: Date? = null
) : AbstractAuditingEntity(), Serializable