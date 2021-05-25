package com.interview.book.rest

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

open class BaseResourceTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    lateinit var username: String
    lateinit var password: String
    lateinit var token: String

    protected fun createNewUser() {
        mockMvc.perform(
            post("/api/users")
                .content(
                    "{\n" +
                            "  \"name\": \"userA\",\n" +
                            "  \"surname\": \"userA_lastname\",\n" +
                            "  \"username\": \"$username\",\n" +
                            "  \"password\": \"$password\",\n" +
                            "  \"date_of_birth\": \"1988-04-20\"\n" +
                            "}"
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
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
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonObj = JSONObject(result.response.contentAsString)
        token = jsonObj["id_token"].toString()
    }

    protected fun deleteUser() {
        mockMvc.perform(
            delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        ).andExpect(MockMvcResultMatchers.status().isOk)
        token = ""
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
}