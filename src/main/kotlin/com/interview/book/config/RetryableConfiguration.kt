package com.interview.book.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.listener.RetryListenerSupport
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate


@Configuration
@EnableRetry
class RetryableConfiguration {
    @Bean
    fun retryTemplate(): RetryTemplate? {
        val retryTemplate = RetryTemplate()
        val fixedBackOffPolicy = FixedBackOffPolicy()
        fixedBackOffPolicy.backOffPeriod = 2000L
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy)
        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = 2
        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.registerListener(RetryListenerSupport())
        return retryTemplate
    }
}