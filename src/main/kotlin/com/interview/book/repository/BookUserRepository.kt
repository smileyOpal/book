package com.interview.book.repository

import com.interview.book.domain.BookUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookUserRepository : JpaRepository<BookUser, Long>