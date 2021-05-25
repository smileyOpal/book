package com.interview.book.rest

import com.interview.book.IntegrationTest
import com.interview.book.service.dto.UserOrderDTO
import net.bytebuddy.utility.RandomString
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*


@AutoConfigureMockMvc
@IntegrationTest
internal class UserResourceTest : BaseResourceTest() {

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
    fun getUser() {
        val result = callGet("/api/users", status().isOk)
        val actual: UserOrderDTO = mapper.readValue(result.response.contentAsString, UserOrderDTO::class.java)
        assertThat(actual.firstName).isEqualTo(testName)
        assertThat(actual.lastName).isEqualTo(testSurname)
        assertThat(formatter.format(actual.dateOfBirth)).isEqualTo(testBirthDate)
        assertThat(actual.orders.size).isEqualTo(0)
    }

    @Test
    fun orderBooks() {
        val resultCreateOrder = callPost(
            path = "/api/users/orders",
            requestBody = "{\n" +
                    "  \"orders\": [\n" +
                    "    {\n" +
                    "      \"bookId\": 1,\n" +
                    "      \"amount\": 1\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"bookId\": 2,\n" +
                    "      \"amount\": 2\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}",
            resultMatcher = status().isOk
        )

        val jsonObj = JSONObject(resultCreateOrder.response.contentAsString)
        assertThat(jsonObj["price"].toString().toFloat()).isGreaterThan(0f)


        val resultUserDetail = callGet("/api/users", status().isOk)
        val actual: UserOrderDTO = mapper.readValue(resultUserDetail.response.contentAsString, UserOrderDTO::class.java)
        assertThat(actual.firstName).isEqualTo(testName)
        assertThat(actual.lastName).isEqualTo(testSurname)
        assertThat(actual.dateOfBirth).isEqualTo(testBirthDate)
        assertThat(actual.orders.size).isEqualTo(2)

        assertThat(actual.orders[0].orderId).isNotNull
        assertThat(actual.orders[0].createDate).isBeforeOrEqualTo(ZonedDateTime.now())

        val firstBook = actual.orders[0].detail[0]
        assertThat(firstBook.bookId).isEqualTo(1)
        assertThat(firstBook.amount).isEqualTo(1)
        assertThat(firstBook.totalPrice).isGreaterThan(0.0)

        val secondBook = actual.orders[0].detail[1]
        assertThat(secondBook.bookId).isEqualTo(2)
        assertThat(secondBook.amount).isEqualTo(2)
        assertThat(secondBook.totalPrice).isGreaterThan(0.0)

    }
}