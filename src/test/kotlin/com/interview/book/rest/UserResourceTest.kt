package com.interview.book.rest

import com.interview.book.IntegrationTest
import io.mockk.InternalPlatformDsl.toArray
import net.bytebuddy.utility.RandomString
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@AutoConfigureMockMvc
@IntegrationTest
internal class UserResourceTest : BaseResourceTest() {

    @BeforeEach
    fun setUp() {
        username = RandomString.make(5)
        password = RandomString.make(10)
        testName = RandomString.make(5)
        testSurname = RandomString.make(5)
        testBirthDate = LocalDate.now().toString()

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
        val jsonObj = JSONObject(result.response.contentAsString)
        assertThat(jsonObj["name"]).isEqualTo(testName)
        assertThat(jsonObj["surname"]).isEqualTo(testSurname)
        assertThat(jsonObj["date_of_birth"]).isEqualTo(testBirthDate)
        assertThat(jsonObj["orders"].toArray()).isEmpty()
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

        var jsonObj = JSONObject(resultCreateOrder.response.contentAsString)
        assertThat(jsonObj["price"].toString().toFloat()).isGreaterThan(0f)


        val resultUserDetail = callGet("/api/users", status().isOk)
        jsonObj = JSONObject(resultUserDetail.response.contentAsString)
        assertThat(jsonObj["name"]).isEqualTo(testName)
        assertThat(jsonObj["surname"]).isEqualTo(testSurname)
        assertThat(jsonObj["date_of_birth"]).isEqualTo(testBirthDate)
        assertThat(jsonObj["orders"].toArray()).isNotEmpty
        assertThat(jsonObj["orders"].toArray().size).isEqualTo(2)
    }
}