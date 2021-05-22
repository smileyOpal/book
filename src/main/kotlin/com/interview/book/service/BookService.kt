package com.interview.book.service

import com.interview.book.domain.Book
import com.interview.book.repository.BookRepository
import com.interview.book.service.dto.BookDTO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

interface BookService {
    fun getBooks(pageable: Pageable): Page<BookDTO>
    fun updateBook(dto: BookSyncDTO, isRecommendBook: Boolean)
}

@Service
class BookServiceImpl(private val bookRepository: BookRepository) : BookService {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val defaultSorting: Sort =
        Sort.by(Sort.Direction.DESC, "isRecommended").and(Sort.by(Sort.Direction.ASC, "bookName"))
    private val defaultSortingFields = listOf("isRecommended", "bookName")

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

    override fun updateBook(dto: BookSyncDTO, isRecommendBook: Boolean) {
        log.info("UPDATING book '${dto.id}' (isRecommendBook=$isRecommendBook)")
        bookRepository.save(Book(dto.id, dto.price, dto.name, dto.author, isRecommendBook))
    }

}