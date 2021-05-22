package com.interview.book

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.Appender
import org.junit.Assert
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.slf4j.LoggerFactory

open class BaseLogCaptureTest {
    val logger = LoggerFactory.getLogger("ROOT") as Logger

    @Mock
    lateinit var mockAppender: Appender<ILoggingEvent>

    @Captor
    private lateinit var captorLoggingEvent: ArgumentCaptor<LoggingEvent>

    protected fun initLogCapture(level: Level) {
        logger.addAppender(mockAppender)
        logger.level = level
        logger.isEnabledFor(level)
    }

    protected fun detachLogAppenders() {
        logger.detachAndStopAllAppenders()
    }

    protected fun assertLoggingMessage(loggingLevel: Level, vararg loggingMessages: String) {
        Mockito.verify(mockAppender, Mockito.atLeastOnce()).doAppend(captorLoggingEvent.capture())
        val allValues = captorLoggingEvent.allValues
        for (loggingMessage in loggingMessages) {
            var foundMatchLogging = false
            for (loggingEvent in allValues) {
                if (loggingEvent.level == loggingLevel && loggingEvent.message != null && loggingEvent.message == loggingMessage) {
                    foundMatchLogging = true
                    break
                }
            }
            Assert.assertTrue("not found [$loggingLevel] $loggingMessage", foundMatchLogging)
        }
    }
}