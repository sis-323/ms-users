package com.users.msusers.api

import com.users.msusers.bl.RelatorBl
import com.users.msusers.bl.TutorBl
import com.users.msusers.dto.RelatorAssignationDto
import com.users.msusers.dto.ResponseDto
import com.users.msusers.dto.TutorAssignationDto
import com.users.msusers.entity.CommitteeMemberReachedLimitException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/assignation")
class AssignationApi (
        @Autowired private val tutorBl: TutorBl,
    @Autowired private val relatorBl: RelatorBl
){

    @PostMapping("/tutor")
    fun assignTutor(@RequestBody tutorAssignationDto: TutorAssignationDto):
            ResponseEntity<ResponseDto<String>> {
        try {
            tutorBl.assignTutor(tutorAssignationDto.userId, tutorAssignationDto.tutorId)
            return ResponseEntity.ok(
                ResponseDto(null, "Tutor assigned successfully", true)
            )
        }
        catch (e: CommitteeMemberReachedLimitException) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
    }

    @PostMapping("/relator")
    fun assignRelator(@RequestBody relatorAssignationDto: RelatorAssignationDto):
            ResponseEntity<ResponseDto<String>> {

        relatorBl.assignRelator(relatorAssignationDto.userId, relatorAssignationDto.relatorId)
        return ResponseEntity.ok(
            ResponseDto(null,"Relator assigned successfully", true))

    }

}