package com.interview.book.rest

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.DateFormat
import java.text.SimpleDateFormat


open class BaseResourceTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    private lateinit var token: String
    lateinit var username: String
    lateinit var password: String

    lateinit var testName: String
    lateinit var testSurname: String
    lateinit var testBirthDate: String

    val mapper = ObjectMapper()
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    protected fun createNewUser() {
        mockMvc.perform(
            post("/api/users")
                .content(
                    "{\n" +
                            "  \"name\": \"$testName\",\n" +
                            "  \"surname\": \"$testSurname\",\n" +
                            "  \"username\": \"$username\",\n" +
                            "  \"password\": \"$password\",\n" +
                            "  \"date_of_birth\": \"$testBirthDate\"\n" +
                            "}"
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    protected fun login() {
        val result = mockMvc.perform(
            post("/login")
                .content(
                    "{\n" +
                            "    \"username\": \"$username\",\n" +
                            "    \"password\": \"$password\"\n" +
                            "}"
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn()

        val jsonObj = JSONObject(result.response.contentAsString)
        token = jsonObj["id_token"].toString()
    }

    protected fun callPost(path: String, requestBody: String, resultMatcher: ResultMatcher): MvcResult {
        return mockMvc.perform(
            post(path)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        ).andExpect(resultMatcher)
            .andReturn()
    }

    protected fun callGet(path: String, resultMatcher: ResultMatcher): MvcResult {
        return mockMvc.perform(
            get(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        ).andExpect(resultMatcher)
            .andReturn()
    }

    protected fun deleteUser() {
        mockMvc.perform(
            delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isOk)
        token = ""
    }
}