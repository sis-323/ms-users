package com.users.msusers.api

import com.users.msusers.bl.RelatorBl
import com.users.msusers.bl.TutorBl
import com.users.msusers.bl.UserBl
import com.users.msusers.dto.*
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
class UserApi @Autowired constructor(
    private val userBl: UserBl,
    @Autowired private val relatorBl: RelatorBl,
    private val tutorBl: TutorBl
) {

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
        return ResponseEntity.ok(ResponseDto(null, "Relator created successfully", true))
    }

    @PostMapping("/tutor")
    fun createTutor(@RequestBody userDto: PersonDto) : ResponseEntity<ResponseDto<String>> {
        try {
            userBl.createUser(userDto, "tutors")
            return ResponseEntity.ok(ResponseDto(null, "User created", true))
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }

    }

    @GetMapping("/students")
    fun getStudents(): ResponseEntity<ResponseDto<List<StudentDto>>> {
        val students = userBl.findStudents()
        return ResponseEntity.ok(ResponseDto(students, "Students found", true))
    }

    @GetMapping("/students/")
    fun getStudentDetails(@RequestParam("kcId") kcId: String): ResponseEntity<ResponseDto<StudentDto>?> {
        val student = userBl.findStudentDetailsByKcId(kcId)
        return ResponseEntity.ok(ResponseDto(student, "Student found", true))
    }

    @GetMapping("/committee")
    fun getRelators(): ResponseEntity<ResponseDto<List<RelatorDto>>> {
        val committee = userBl.findCommittee()
        return ResponseEntity.ok(ResponseDto(committee, "Relators found", true))
    }

    @GetMapping("/tutors")
    fun getTutors(): ResponseEntity<ResponseDto<List<RelatorDto>>>
    {
        val tutors = userBl.findTutors()
        return ResponseEntity.ok(ResponseDto(tutors, "Tutors found", true))
    }

    @PutMapping("/relator")
    fun deleteRelator(@RequestParam("kcId") kcUUID: String): ResponseEntity<ResponseDto<String>> {
        try {
            relatorBl.deleteRelator(kcUUID)
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "Relator deleted", true))
    }

    @PutMapping("/tutor")
    fun deleteTutor(@RequestParam("kcId") kcUUID: String): ResponseEntity<ResponseDto<String>> {
        try {
            tutorBl.deleteTutor(kcUUID)
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "Tutor deleted", true))
    }

    @GetMapping("/")
    fun getUserDetails(@RequestParam("kcId") kcId: String): ResponseEntity<ResponseDto<PersonDto>> {
        val user = userBl.findUserDetailsByKcId(kcId)
        return ResponseEntity.ok(ResponseDto(user, "User found", true))
    }

    @PutMapping("/")
    fun updateUser(
        @RequestParam("kcId") kcId: String,
        @RequestBody userDto: PersonDto
    ): ResponseEntity<ResponseDto<String>> {
        try {
            userBl.updateUser(kcId, userDto)
        }
        catch (e: Exception) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
        return ResponseEntity.ok(ResponseDto(null, "User updated", true))
    }





}