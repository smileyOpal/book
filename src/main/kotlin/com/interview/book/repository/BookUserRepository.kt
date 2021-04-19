package com.interview.book.repository

import com.interview.book.domain.BookUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookUserRepository : JpaRepository<BookUser, Long> {
    fun findOneByUsername(username: String): Optional<BookUser>
    fun deleteByUsername(username: String)
}