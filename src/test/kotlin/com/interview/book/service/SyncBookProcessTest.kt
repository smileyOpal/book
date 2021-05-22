package com.interview.book.service

import ch.qos.logback.classic.Level
import com.interview.book.BaseLogCaptureTest
import com.interview.book.IntegrationTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc


@AutoConfigureMockMvc
@IntegrationTest
internal class SyncBookProcessTest : BaseLogCaptureTest() {

    @Autowired
    private lateinit var syncBookProcess: SyncBookProcess

    @BeforeEach
    fun setUp() {
        initLogCapture(Level.INFO)
    }

    @AfterEach
    fun tearDown() {
        detachLogAppenders()
    }

    @Test
    fun syncBookData() {
        syncBookProcess.syncBookData()
        assertLoggingMessage(Level.INFO, "START sync book data")
        assertLoggingMessage(Level.INFO, "UPDATING book '1'")
        assertLoggingMessage(Level.INFO, "UPDATING book '2'")
        assertLoggingMessage(Level.INFO, "UPDATING book '3'")
        assertLoggingMessage(Level.INFO, "UPDATING book '4'")
        assertLoggingMessage(Level.INFO, "UPDATING book '5'")
        assertLoggingMessage(Level.INFO, "UPDATING book '6'")
        assertLoggingMessage(Level.INFO, "UPDATING book '7'")
        assertLoggingMessage(Level.INFO, "UPDATING book '8'")
        assertLoggingMessage(Level.INFO, "UPDATING book '9'")
        assertLoggingMessage(Level.INFO, "UPDATING book '10'")
        assertLoggingMessage(Level.INFO, "UPDATING book '11'")
        assertLoggingMessage(Level.INFO, "UPDATING book '12'")
        assertLoggingMessage(Level.INFO, "END sync book data with 12 record(s)")
    }
}