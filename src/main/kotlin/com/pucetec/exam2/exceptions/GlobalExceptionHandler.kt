package com.pucetec.exam2.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ExceptionResponse(
    val message: String,
    val source: String
)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleSpaceNotFoundException(ex: SpaceNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message?: "Espacio no encontrado", "Database")
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(SpaceOccupiedException::class)
    fun handleSpaceOccupiedException(ex: SpaceOccupiedException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message?: "Espacio ocupado", "Database")
        return ResponseEntity(response, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(TicketClosedException::class)
    fun handleTicketClosedException(ex: TicketClosedException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message?: "No se pueden adquirir tickets", "Database")
        return ResponseEntity(response, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFoundException(ex: TicketNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse (ex.message?: "No se ha encontrado un ticket", "Database")
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}