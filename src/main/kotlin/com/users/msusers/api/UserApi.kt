package com.users.msusers.api

import com.users.msusers.bl.RelatorBl
import com.users.msusers.bl.UserBl
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.RelatorDto
import com.users.msusers.dto.ResponseDto
import com.users.msusers.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = ["*"])
class UserApi @Autowired constructor(private val userBl: UserBl,
    @Autowired private val relatorBl: RelatorBl) {

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


    @PostMapping("/relator")
    fun createRelator(@RequestBody userDto: PersonDto) : ResponseEntity<ResponseDto<String>> {
        try {
            userBl.createUser(userDto, "relators")
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "User created", true))
    }

    @PostMapping("/tutor")
    fun createTutor(@RequestBody userDto: PersonDto) : ResponseEntity<ResponseDto<String>> {
        try {
            userBl.createUser(userDto, "tutors")
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "User created", true))
    }

    @GetMapping("/students")
    fun getStudents(): ResponseEntity<ResponseDto<List<UserDto>>> {
        val students = userBl.findStudents()
        return ResponseEntity.ok(ResponseDto(students, "Students found", true))
    }
    @GetMapping("/relators")
    fun getRelators(): ResponseEntity<ResponseDto<List<RelatorDto>>> {
        val students = relatorBl.findRelators()
        return ResponseEntity.ok(ResponseDto(students, "Relators found", true))
    }

    @PutMapping("/relator")
    fun deleteRelator(@RequestParam kcUUID: String): ResponseEntity<ResponseDto<String>> {
        try {
            relatorBl.deleteRelator(kcUUID)
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "Relator deleted", true))
    }





}