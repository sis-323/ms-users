package com.users.msusers.bl

import com.users.msusers.dao.AssignationRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.entity.Assignation
import com.users.msusers.exception.CommitteeMemberReachedLimitException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RelatorBl (
     @Autowired private val assignationRepository: AssignationRepository,
        @Autowired private val userRepository: UserRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RelatorBl::class.java)
    }

    fun assignRelator(userKcId: String, relatorKcId: String) {
        logger.info("Start assigning relator to user: $userKcId")

        val user = userRepository.findByIdKc(userKcId)

        val relator = userRepository.findByIdKc(relatorKcId)

        logger.info("Verifying relator student limit for relator: ${relator.name}")

        if (checkRelatorStudentLimit(relator.idPerson)) {
            if(getAssignation(user.idPerson) == null){
                val assignation = Assignation()
                assignation.status = true
                assignation.studentId = user
                assignation.relatorId = relator

                assignationRepository.save(assignation)

                logger.info("Relator assigned to user: $userKcId")
                }
            else{
                val assignation = checkAssignation(user.idPerson, relator.idPerson) as Assignation
                assignation.relatorId = relator
                assignationRepository.save(assignation)
            }

        }else{
            logger.warn("Relator with ID: $relatorKcId has reached the limit of students")
            throw CommitteeMemberReachedLimitException("Relator has reached the limit of students")
        }

    }


    fun deleteRelator(userKcUUID: String){
        logger.debug("Start deleting relator: $userKcUUID")
        val relator = userRepository.findByIdKc(userKcUUID)
        relator.status = false
        userRepository.save(relator)
        logger.info("Relator deleted: $userKcUUID")

    }

    fun findStudentsByRelator(relatorKcId: String): List<PersonDto>{
        val relator = userRepository.findByIdKc(relatorKcId)
        val assignations = assignationRepository.findAllByRelatorId(relator)

        val students = mutableListOf<PersonDto>()

        for (assignation in assignations){
            students.add(PersonDto(
                assignation.studentId?.idPerson, assignation.studentId?.name,
                assignation.studentId?.lastName, assignation.studentId?.motherLastName,
                assignation.studentId?.email, assignation.studentId?.phoneNumber, null, null))
        }
        return students
    }




    private fun checkRelatorStudentLimit(tutorId: Long): Boolean {
        val tutor = userRepository.findById(tutorId).get()
        val assignations = assignationRepository.findAllByTutorId(tutor)
        return assignations.size < 2

    }

    private fun checkAssignation(studentId: Long, relatorId: Long): Any {
        val student = userRepository.findById(studentId).orElse(null)
        val assignationExists =
        assignationRepository.existsByStudentId(student)
        if (assignationExists){
            return assignationRepository.findByStudentId(student)
        }
        return false

    }


    private fun getAssignation(studentId: Long): Any? {
        if (checkAssignation(studentId)) {
            val student = userRepository.findById(studentId).get()
            return assignationRepository.findByStudentId(student)
        }
        return null

    }

    private fun checkAssignation(studentId: Long): Boolean {
        val student = userRepository.findById(studentId).get()
        return assignationRepository.existsByStudentId(student)
    }


}