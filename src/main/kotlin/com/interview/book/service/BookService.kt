package com.interview.book.service

import com.interview.book.repository.BookRepository
import com.interview.book.service.dto.BookDTO
import com.interview.book.service.dto.CalculatedOrderDTO
import com.interview.book.service.dto.CreateOrderDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

interface BookService {
    fun getBooks(pageable: Pageable): Page<BookDTO>
    fun createOrder(username: String, request: CreateOrderDTO): CalculatedOrderDTO
}

@Service
class BookServiceImpl(val bookRepository: BookRepository) : BookService {

    val defaultSorting: Sort =
        Sort.by(Sort.Direction.DESC, "isRecommended").and(Sort.by(Sort.Direction.ASC, "bookName"))
    val defaultSortingFields = listOf("isRecommended", "bookName")

    override fun getBooks(pageable: Pageable): Page<BookDTO> {
        val builtPageable = if (pageable.sort.isEmpty) {
            PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                defaultSorting
            )
        } else {
            val sortFields = pageable.sort.filter { !defaultSortingFields.contains(it.property) }.toList()
            PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                defaultSorting.and(Sort.by(sortFields))
            )
        }
        return bookRepository.findAll(builtPageable).map {
            BookDTO(it.id, it.bookName, it.price, it.authorName, it.isRecommended)
        }
    }

    override fun createOrder(username: String, request: CreateOrderDTO): CalculatedOrderDTO {
        val books = bookRepository.findByIdIsIn(request.orders)
        return CalculatedOrderDTO(books.sumByDouble { it.price })
    }

}