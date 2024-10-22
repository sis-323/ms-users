package com.users.msusers.config

import com.users.msusers.dto.ResponseDto
import com.users.msusers.exception.CommitteeMemberReachedLimitException
import com.users.msusers.exception.CustomNotFoundException
import com.users.msusers.exception.UserAlreadyExistsException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerController {
    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionHandlerController::class.java.name)
    }

    @ExceptionHandler(CustomNotFoundException::class)
    fun handleNotFoundException(e: CustomNotFoundException): ResponseEntity<ResponseDto<Nothing>> {
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): ResponseEntity<ResponseDto<Nothing>> {
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler
    fun handleTutorReachedLimitException(e: CommitteeMemberReachedLimitException): ResponseEntity<ResponseDto<Nothing>> {
        return ResponseEntity(ResponseDto(null, e.message!!, false), HttpStatus.BAD_REQUEST)
    }
}