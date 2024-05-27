package com.users.msusers.api

import com.users.msusers.bl.RelatorBl
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.ResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users/relators")

class RelatorApi (
    @Autowired private val relatorBl: RelatorBl
){

    @GetMapping("/{kcId}/students")
    fun getStudentsByRelatorId(@PathVariable kcId: String): ResponseEntity<ResponseDto<List<PersonDto>>> {
        val students = relatorBl.findStudentsByRelator(kcId)
        return ResponseEntity.ok(ResponseDto(students, "Students found", true))
    }

}