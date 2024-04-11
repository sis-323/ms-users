package com.users.msusers.api

import com.users.msusers.bl.UserBl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserApi @Autowired constructor(private val userBl: UserBl) {

    @PostMapping("")
    fun createUser(@RequestBody name: String): ResponseEntity<String>{
        userBl.createUser(name)
        return ResponseEntity.ok("User created")
    }
}