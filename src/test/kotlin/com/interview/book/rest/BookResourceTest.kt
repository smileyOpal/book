package com.interview.book.rest

import com.interview.book.IntegrationTest
import net.bytebuddy.utility.RandomString
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*


@AutoConfigureMockMvc
@IntegrationTest
internal class BookResourceTest : BaseResourceTest() {

    @BeforeEach
    fun setUp() {
        username = RandomString.make(5)
        password = RandomString.make(10)
        testName = RandomString.make(5)
        testSurname = RandomString.make(5)
        testBirthDate = formatter.format(Date.from(Instant.now()))

        createNewUser()
        login()
    }

    @AfterEach
    fun tearDown() {
        deleteUser()
    }

    @Test
    fun `Assert get books with no token`() {
        mockMvc.perform(
            get("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `Assert get books with token`() {
        val result = callGet("/api/books", status().isOk)

        val jsonArray = JSONArray(result.response.contentAsString)
        assertThat(jsonArray.length()).isEqualTo(12)
    }
}