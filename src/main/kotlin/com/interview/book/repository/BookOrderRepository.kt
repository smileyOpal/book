package com.interview.book.repository

import com.interview.book.domain.BookOrder
import org.springframework.data.jpa.repository.JpaRepository

interface BookOrderRepository : JpaRepository<BookOrder, Long> {
    fun findByUserId(userId: Long): List<BookOrder>
}