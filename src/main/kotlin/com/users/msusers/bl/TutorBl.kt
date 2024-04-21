package com.users.msusers.bl

import com.users.msusers.dao.AssignationRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.entity.Assignation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TutorBl constructor(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val assignationRepository: AssignationRepository,

) {
    companion object {
        private val logger = LoggerFactory.getLogger(TutorBl::class.java)
    }

    fun assignTutor(userId: Long, tutorId: Long) {
        logger.debug("Start assigning tutor to user: $userId")

        val user = userRepository.findById(userId).get()

        val tutor = userRepository.findById(tutorId).get()

        logger.info("Verifying tutor student limit for tutor: ${tutor.name}")

        if (checkTutorStudentLimit(tutorId)) {
            val assignation = Assignation()
            assignation.status = true
            assignation.studentId = user
            assignation.tutorId = tutor

            assignationRepository.save(assignation)

            logger.info("Tutor assigned to user: $userId")
        } else {
            logger.warn("Tutor with ID: $tutorId has reached the limit of students")
            throw Exception("Tutor has reached the limit of students")
        }


    }

    private fun checkTutorStudentLimit(tutorId: Long): Boolean {
        val tutor = userRepository.findById(tutorId).get()
        val assignations = assignationRepository.findAllByTutorId(tutor)
        return assignations.size < 3

    }

}