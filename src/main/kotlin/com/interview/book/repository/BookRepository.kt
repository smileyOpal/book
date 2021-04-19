package com.interview.book.repository

import com.interview.book.domain.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    fun findByIdIsIn(ids: List<Long>): List<Book>
}