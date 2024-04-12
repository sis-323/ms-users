package com.users.msusers.api

import com.users.msusers.bl.UserBl
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.ResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserApi @Autowired constructor(private val userBl: UserBl) {

    @PostMapping("/student")
    fun createUser(@RequestBody userDto: PersonDto): ResponseEntity<String>{
        userBl.createUser(userDto, "students")
        return ResponseEntity.ok("User created")
    }

    @PostMapping("/professor")
    fun createProfessor(@RequestBody userDto: PersonDto) : ResponseEntity<ResponseDto<String>> {
        try {
            userBl.createUser(userDto, "professors")

        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "User created", true))
    }

}