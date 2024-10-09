package com.lezhin.api.handler

import com.lezhin.api.exception.AdultException
import com.lezhin.api.exception.AuthException
import com.lezhin.api.exception.ComicException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun exception(
        e: Exception,
    ): ResponseEntity<MutableMap<String, String>> {
        val errorCode = "SYSTEM_ERROR"
        val errorMessage = e.message ?: "Unknown error occurred"

        val errors = mutableMapOf(
            "code" to errorCode,
            "message" to errorMessage,
        )
        logger.error { "System error: $errorMessage" }
        return ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ComicException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun employeeException(
        e: ComicException,
    ): ResponseEntity<MutableMap<String, String>> {
        val errorCode = e.code
        val errorMessage = e.message ?: "comic error occurred"

        val errors = mutableMapOf(
            "code" to errorCode,
            "message" to errorMessage,
        )
        logger.error { "comic error: $errorMessage" }
        return ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AdultException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun adultException(
        e: AuthException,
    ): ResponseEntity<MutableMap<String, String>> {
        val errorCode = "ADULT_ERROR"
        val errorMessage = e.message ?: "Adult error occurred"

        val errors = mutableMapOf(
            "code" to errorCode,
            "message" to errorMessage,
        )
        logger.error { "Adult error: $errorMessage" }
        return ResponseEntity(errors, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AuthException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun authException(
        e: AuthException,
    ): ResponseEntity<MutableMap<String, String>> {
        val errorCode = "AUTH_ERROR"
        val errorMessage = e.message ?: "Authentication error occurred"

        val errors = mutableMapOf(
            "code" to errorCode,
            "message" to errorMessage,
        )
        logger.error { "Authentication error: $errorMessage" }
        return ResponseEntity(errors, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<MutableMap<String, Any>> {
        val errors = mutableMapOf<String, Any>()
        val fieldErrors = ex.bindingResult.allErrors.map { error ->
            val fieldError = error as FieldError
            fieldError.field to (error.defaultMessage ?: "Invalid value")
        }
        errors["code"] = "VALIDATION_ERROR"
        errors["message"] = "유효성검사 에러"
        errors["fieldErrors"] = fieldErrors.toMap()

        logger.error { "Validation error: $fieldErrors" }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
    ): ResponseEntity<MutableMap<String, String>> {
        val errorCode = "INVALID_REQUEST_BODY_ERROR"
        val errorMessage = ex.message ?: "요청 형식 에러"

        val errors = mutableMapOf(
            "code" to errorCode,
            "message" to errorMessage,
        )
        logger.error { "Request body error: $errorMessage" }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

}