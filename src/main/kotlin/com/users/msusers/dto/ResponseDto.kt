package com.users.msusers.dto

data class ResponseDto<T>(
        val data: T?,
        val message: String,
        val successful: Boolean
)