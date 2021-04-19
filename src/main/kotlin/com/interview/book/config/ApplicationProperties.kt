package com.interview.book.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Properties specific to Book.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties {
    var jwt = Jwt()
}

class Jwt {
    lateinit var base64Secret: String
    var tokenValidityInSeconds: Long = 3600
    var tokenValidityInSecondsForRememberMe: Long = 25600
}