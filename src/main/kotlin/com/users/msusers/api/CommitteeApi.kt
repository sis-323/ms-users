package com.users.msusers.api

import com.users.msusers.bl.CommitteeBl
import com.users.msusers.dto.RelatorDto
import com.users.msusers.dto.ResponseDto
import com.users.msusers.dto.StudentDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users/committee")
@CrossOrigin(origins = ["*"])
class CommitteeApi @Autowired constructor(
    private val committeeBl: CommitteeBl
) {

    @GetMapping("/members/{kcId}")
    fun getCommitteeMemberDetails( @PathVariable("kcId") kcId: String): ResponseEntity<ResponseDto<RelatorDto>> {
        val committeeMember = committeeBl.findCommitteeMemberDetails(kcId)
        return ResponseEntity.ok(ResponseDto(committeeMember, "Committee member found", true))
    }

    @GetMapping("/members/students/{kcId}")
    fun getTutorStudents( @PathVariable("kcId") kcId: String,
                          @RequestParam("type") type: String): ResponseEntity<ResponseDto<List<StudentDto>>> {
        val tutorStudents = committeeBl.findStudentsByCommitteeMemberKcId(kcId,type)
        return ResponseEntity.ok(ResponseDto(tutorStudents, "Tutor students found", true))
    }




}