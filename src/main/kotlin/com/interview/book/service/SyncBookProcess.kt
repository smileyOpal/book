package com.interview.book.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.interview.book.config.ApplicationProperties
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.Serializable

@Component
class SyncBookProcess(
    applicationProperties: ApplicationProperties,
    val bookService: BookService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val restTemplate = RestTemplate()
    private val dataSyncProperties = applicationProperties.dataSync

    @Scheduled(
        cron = "\${application.data-sync.cronExpression}",
        zone = "\${application.data-sync.zone}"
    )
    fun syncBookData() {
        log.info("START sync book data")
        val recommendBookIds = getRecommendBooks()
        val books = restTemplate.getForObject(dataSyncProperties.bookEndpoint, Array<BookSyncDTO>::class.java)
        books?.toList()?.forEach { book ->
            bookService.updateBook(book, recommendBookIds.firstOrNull { book.id == it } != null)
        }
        log.info("END sync book data with ${books?.size ?: 0} record(s)")
    }

    private fun getRecommendBooks(): List<Long> {
        return restTemplate.getForObject(dataSyncProperties.bookRecommendEndpoint, Array<BookSyncDTO>::class.java)
            ?.map { it.id } ?: emptyList()
    }

}

data class BookSyncDTO(
    val id: Long,
    @JsonProperty("book_name") val name: String,
    val price: Double = 0.0,
    @JsonProperty("author_name") val author: String
) : Serializable