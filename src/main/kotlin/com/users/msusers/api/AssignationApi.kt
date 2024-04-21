package com.users.msusers.api

import com.users.msusers.bl.TutorBl
import com.users.msusers.dto.ResponseDto
import com.users.msusers.dto.TutorAssignationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/assignation")
class AssignationApi (
        @Autowired private val tutorBl: TutorBl
){

    @PostMapping("/tutor")
    fun assignTutor(@RequestBody tutorAssignationDto: TutorAssignationDto):
            ResponseEntity<ResponseDto<String>> {

        tutorBl.assignTutor(tutorAssignationDto.userId, tutorAssignationDto.tutorId)
        return ResponseEntity.ok(
            ResponseDto(null,"Tutor assigned successfully", true))

    }
}