package com.users.msusers.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.users.msusers.dto.ResponseDto
import com.users.msusers.entity.CommitteeMemberReachedLimitException
import com.users.msusers.exception.CustomNotFoundException
import com.users.msusers.exception.UserAlreadyExistsException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.ws.rs.NotFoundException

@ControllerAdvice
class ExceptionHandlerController {
    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionHandlerController::class.java.name)
    }

    @ExceptionHandler(CustomNotFoundException::class)
    fun handleNotFoundException(e: CustomNotFoundException): ResponseEntity<ResponseDto<Nothing>> {
        logger.error("Error: ${e.message}")
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): ResponseEntity<ResponseDto<Nothing>> {
        logger.error("Error: ${e.message}")
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler
    fun handleTutorReachedLimitException(e: CommitteeMemberReachedLimitException): ResponseEntity<ResponseDto<Nothing>> {
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.BAD_REQUEST)
    }
}