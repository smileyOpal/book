package com.interview.book.rest

import com.interview.book.service.BookService
import com.interview.book.service.dto.BookDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

/**
 * A Resource/Controller layer that defined request interfaces and receives requests
 */
@RestController
@RequestMapping("/api")
@Api("Book resource")
class BookResource(val bookService: BookService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/books")
    @ApiOperation(notes = "List all books", value = "List books")
    @ApiImplicitParams(
        ApiImplicitParam(name = "sort", paramType = "query", dataType = "string", allowMultiple = true),
        ApiImplicitParam(name = "page", paramType = "query", dataType = "int", defaultValue = "0", example = "0"),
        ApiImplicitParam(name = "size", paramType = "query", dataType = "int", defaultValue = "20", example = "20")
    )
    fun getBooks(@ApiIgnore pageable: Pageable): ResponseEntity<List<BookDTO>> {
        log.info("Request list book with pageable $pageable")
        val result = bookService.getBooks(pageable)
        val httpHeaders = HttpHeaders()
        httpHeaders["x-total-count"] = result.totalElements.toString()
        httpHeaders["x-total-page"] = result.totalPages.toString()
        httpHeaders["x-current-page"] = result.number.toString()
        httpHeaders["x-page-size"] = result.size.toString()
        return ResponseEntity.ok()
            .headers(httpHeaders)
            .body(result.content)
    }
}