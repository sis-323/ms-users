package com.users.msusers.bl

import com.users.msusers.dao.AssignationRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.RelatorDto
import com.users.msusers.dto.StudentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommitteeBl constructor(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val assignationRepository: AssignationRepository
){
    companion object{
        val logger: Logger = LoggerFactory.getLogger(CommitteeBl::class.java)
    }

    //TODO: add tutor and relator to the studentDto
    fun findStudentsByTutorKcId(kcId: String): List<StudentDto>{
        logger.info("Finding students by tutor kc id $kcId")
        val tutor = userRepository.findByIdKc(kcId)
        val students = assignationRepository.findAllByTutorId(tutor)
        val studentsDto = mutableListOf<StudentDto>()
        students.forEach {
            studentsDto.add(StudentDto(it.studentId?.idKc,
                it.studentId?.name, it.studentId?.lastName,
                it.studentId?.motherLastName, it.studentId?.email,
                it.studentId?.phoneNumber,it.tutorId?.name, it.relatorId?.name,
                it.studentId?.idKc))
        }
        return studentsDto
    }


    /**
     * This method is used to find the details of a committee member by their keycloak id
     */
    fun findCommitteeMemberDetails(kcId: String): RelatorDto{
        val user = userRepository.findByIdKc(kcId)
        return RelatorDto(user.idKc, user.name, user.lastName, user.motherLastName,user.email,
            user.phoneNumber, user.group)
    }

}