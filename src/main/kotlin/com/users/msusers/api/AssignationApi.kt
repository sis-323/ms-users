package com.users.msusers.api

import com.users.msusers.bl.RelatorBl
import com.users.msusers.bl.TutorBl
import com.users.msusers.dto.CommitteeAssignationDto
import com.users.msusers.dto.ResponseDto
import com.users.msusers.dto.TutorAssignationDto
import com.users.msusers.entity.CommitteeMemberReachedLimitException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/assignation")
@CrossOrigin(origins = ["*"])
class AssignationApi (
        @Autowired private val tutorBl: TutorBl,
    @Autowired private val relatorBl: RelatorBl
){

    @PostMapping("/tutor")
    fun assignTutor(@RequestBody tutorAssignationDto: CommitteeAssignationDto):
            ResponseEntity<ResponseDto<String>> {
        try {
            tutorBl.assignTutor(tutorAssignationDto.userKcId, tutorAssignationDto.committeeMemberKcId)
            return ResponseEntity.ok(
                ResponseDto(null, "Tutor assigned successfully", true)
            )
        }
        catch (e: CommitteeMemberReachedLimitException) {
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))
        }
    }

    @PostMapping("/relator")
    fun assignRelator(@RequestBody relatorAssignationDto: CommitteeAssignationDto):
            ResponseEntity<ResponseDto<String>> {
        try{
            relatorBl.assignRelator(relatorAssignationDto.userKcId, relatorAssignationDto.committeeMemberKcId)
            return ResponseEntity.ok(
                ResponseDto(null,"Relator assigned successfully", true))
        }
        catch (e: CommitteeMemberReachedLimitException){
            return ResponseEntity.badRequest().body(ResponseDto(null, e.message!!, false))

        }
    }



}