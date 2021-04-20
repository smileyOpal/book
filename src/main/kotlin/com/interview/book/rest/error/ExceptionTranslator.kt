package com.interview.book.rest.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait

@ControllerAdvice
class ExceptionTranslator : ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler
    fun handleBadRequestException(ex: BadRequestException, request: NativeWebRequest): ResponseEntity<Problem> {
        return create(ex, request)
    }

    @ExceptionHandler
    fun handleBadRequestException(ex: NotFoundException, request: NativeWebRequest): ResponseEntity<Problem> {
        return create(ex, request)
    }

    @ExceptionHandler
    fun handleBadRequestException(ex: ForbiddenException, request: NativeWebRequest): ResponseEntity<Problem> {
        return create(ex, request)
    }

    @ExceptionHandler
    fun handleBadRequestException(ex: InternalServerException, request: NativeWebRequest): ResponseEntity<Problem> {
        return create(ex, request)
    }
}