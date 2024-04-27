package com.users.msusers.bl

import com.users.msusers.dao.AssignationRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.RelatorDto
import com.users.msusers.entity.Assignation
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

    fun assignRelator(userId: Long, relatorId: Long) {
        logger.debug("Start assigning relator to user: $userId")

        val user = userRepository.findById(userId).get()

        val relator = userRepository.findById(relatorId).get()

        logger.info("Verifying relator student limit for relator: ${relator.name}")

        if (checkRelatorStudentLimit(relatorId)) {
            if(getAssignation(userId) == null){
                val assignation = Assignation()
                assignation.status = true
                assignation.studentId = user
                assignation.relatorId = relator

                assignationRepository.save(assignation)

                logger.info("Relator assigned to user: $userId")
                }
            else{
                val assignation = checkAssignation(userId, relatorId) as Assignation
                assignation.relatorId = relator
                assignationRepository.save(assignation)

            }
        }

    }

    fun findRelators(): List<RelatorDto>{
        val relators = userRepository.findRelators()
        val relatorDtos = mutableListOf<RelatorDto>()
        relators.forEach {
            relatorDtos.add(RelatorDto(it.idKc, it.name, it.lastName, it.motherLastName, it.email, it.phoneNumber))
        }
        return relatorDtos
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