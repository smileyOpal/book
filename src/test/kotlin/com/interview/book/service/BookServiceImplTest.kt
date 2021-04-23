package com.interview.book.service

import com.interview.book.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.domain.PageRequest

@AutoConfigureMockMvc
@IntegrationTest
internal class BookServiceImplTest {

    @Autowired
    private lateinit var bookService: BookService

    @Test
    fun `Assert correct sorting result when get all books `() {
        val actual = bookService.getBooks(PageRequest.of(0, 12))
        assertThat(actual.content).isNotEmpty
        assertThat(actual.content.size).isEqualTo(12)

        val actualContent = actual.content
        assertThat(actualContent[0].id).isEqualTo(5)
        assertThat(actualContent[0].name).isEqualTo("An American Princess: The Many Lives of Allene Tew")

        assertThat(actualContent[1].id).isEqualTo(4)
        assertThat(actualContent[1].name).isEqualTo("The Great Alone: A Novel Kristin Hannah")

        assertThat(actualContent[2].id).isEqualTo(1)
        assertThat(actualContent[2].name).isEqualTo("Before We Were Yours: A Novel")

        assertThat(actualContent[3].id).isEqualTo(3)
        assertThat(actualContent[3].name).isEqualTo("Giraffes Can't Dance")

        assertThat(actualContent[4].id).isEqualTo(12)
        assertThat(actualContent[4].name).isEqualTo("Harry Potter - A History of Magic")

        assertThat(actualContent[5].id).isEqualTo(8)
        assertThat(actualContent[5].name).isEqualTo("Have You Filled A Bucket Today?")

        assertThat(actualContent[6].id).isEqualTo(6)
        assertThat(actualContent[6].name).isEqualTo("Medical Medium Life-Changing Foods")

        assertThat(actualContent[7].id).isEqualTo(11)
        assertThat(actualContent[7].name).isEqualTo("The Alice Network")

        assertThat(actualContent[8].id).isEqualTo(10)
        assertThat(actualContent[8].name).isEqualTo("The Hate U Give")

        assertThat(actualContent[9].id).isEqualTo(9)
        assertThat(actualContent[9].name).isEqualTo("The Very Hungry Caterpillar")

        assertThat(actualContent[10].id).isEqualTo(7)
        assertThat(actualContent[10].name).isEqualTo("Vegan 100")

        assertThat(actualContent[11].id).isEqualTo(2)
        assertThat(actualContent[11].name).isEqualTo("When Never Comes")
    }
}