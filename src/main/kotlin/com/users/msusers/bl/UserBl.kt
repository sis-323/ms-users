package com.users.msusers.bl

import com.users.msusers.dao.AssignationRepository
import com.users.msusers.dao.ModalityRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.RelatorDto
import com.users.msusers.dto.StudentDto
import com.users.msusers.dto.UserDto
import com.users.msusers.entity.Modality
import com.users.msusers.entity.Person
import com.users.msusers.exception.CustomNotFoundException
import com.users.msusers.exception.UserAlreadyExistsException
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Date
import javax.ws.rs.ClientErrorException


@Service
class UserBl @Autowired constructor(
        private val userRepository: UserRepository,
        private val modalityRepository: ModalityRepository,
        private val keycloak: Keycloak,
        private val assignationRepository: AssignationRepository
        ) {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(UserBl::class.java.name)
    }

    @Value("\${keycloak.credentials.realm}")
    private val realm: String? = null

    fun createUser(personDto: PersonDto, groupName: String){

        logger.info("Creating user: ${personDto.email} in group: $groupName")

        val passwordRepresentation = preparePasswordRepresentation(personDto.password!!)
        val userRepresentation = prepareUserRepresentation(personDto, passwordRepresentation, groupName)

        //val person = userRepository.findByEmail(personDto.email!!)
        val response = keycloak.realm(realm).users().create(userRepresentation)

        if (response.status != 201) {
            logger.error("Error creating user: ${response.statusInfo}")
            throw ClientErrorException(response)
        }

        val userUuid = response.location.path.split("/").last()
        logger.info("User created successfully. UUID: $userUuid")

        val user = Person()
        val modality = this.modalityRepository.findByIdModality(personDto.modalityId!!)
        user.modality = modality
        user.name = personDto.name!!
        user.lastName = personDto.lastName!!
        user.motherLastName = personDto.motherLastName!!
        user.email = personDto.email!!
        user.phoneNumber = personDto.phoneNumber!!
        user.group = groupName
        user.status = true
        user.idKc = userUuid

        user.semester = getSemester(LocalDate.now())
        userRepository.save(user)
        logger.info("User saved in the database.")
    }

    fun findStudents(): List<StudentDto> {
        val students = userRepository.findStudents()
        val result = mutableListOf<StudentDto>()
        students.forEach {
            val modality = modalityRepository.findByIdModality(it.modality!!.idModality)
            val tutorExists = assignationRepository.tutorExistsByStudentIdIdKc(it.idKc)
            val relatorExists = assignationRepository.relatorExistsByStudentIdIdKc(it.idKc)
            val tutor = if (tutorExists) assignationRepository.findByStudentIdIdKc(it.idKc).tutorId?.name else "Sin asignar"
            val relator = if (relatorExists) assignationRepository.findByStudentIdIdKc(it.idKc).relatorId?.name else "Sin asignar"
            result.add(
                    StudentDto(
                            modality.modality,
                            it.name,
                            it.lastName,
                            it.motherLastName,
                            it.email,
                            it.phoneNumber,
                            tutor,
                            relator,
                            it.idKc,
                    )
            )
        }
        return result
    }

    fun findStudentDetailsByKcId(kcId: String): StudentDto {
        val student = userRepository.findByIdKc(kcId)
        val modality = modalityRepository.findByIdModality(student.modality!!.idModality)
        val tutorExists = assignationRepository.tutorExistsByStudentIdIdKc(student.idKc)
        val relatorExists = assignationRepository.relatorExistsByStudentIdIdKc(student.idKc)
        val tutor = if (tutorExists) assignationRepository.findByStudentIdIdKc(student.idKc).tutorId?.name else "Sin asignar"
        val relator = if (relatorExists) assignationRepository.findByStudentIdIdKc(student.idKc).relatorId?.name else "Sin asignar"
        return StudentDto(
                modality.modality,
                student.name,
                student.lastName,
                student.motherLastName,
                student.email,
                student.phoneNumber,
                tutor,
                relator,
                student.idKc
        )
    }

    fun findUserDetailsByKcId(kcId: String): PersonDto {
        val user = userRepository.findByIdKc(kcId)
        return PersonDto(
                user.idPerson,
                user.name,
                user.lastName,
                user.motherLastName,
                user.email,
                user.phoneNumber,
                user.modality?.modality,
                user.group

        )
    }

    // TODO: Refactor relator dto
    fun findCommittee(): List<RelatorDto>{
        val committee = userRepository.findCommitteeMembers()
        return committee.map {
            RelatorDto(
                    it.idKc,
                    it.name,
                    it.lastName,
                    it.motherLastName,
                    it.email,
                    it.phoneNumber,
                    it.group
            )
        }
    }

    fun findTutors(): List<RelatorDto>{
        val tutors = userRepository.findAllTutors()
        return tutors.map {
            RelatorDto(
                    it.idKc,
                    it.name,
                    it.lastName,
                    it.motherLastName,
                    it.email,
                    it.phoneNumber,
                    it.group
            )
        }
    }

    fun updateUser(kcId: String, userDto: PersonDto): PersonDto {
        logger.info("Updating user: $kcId")
        try{
            val user = userRepository.findByIdKc(kcId)

            user.name = userDto.name!!
            user.lastName = userDto.lastName!!
            user.motherLastName = userDto.motherLastName!!
            user.phoneNumber = userDto.phoneNumber!!

            userRepository.save(user)

            logger.info("User updated successfully")
            return PersonDto(
                user.idPerson,
                user.name,
                user.lastName,
                user.motherLastName,
                user.email,
                user.phoneNumber,
                user.modality?.modality,
                user.group
            )
        } catch (e: Exception) {
            logger.error("Error updating user: ${e.message}")
            throw CustomNotFoundException("User not found")
        }
    }




    private fun preparePasswordRepresentation(
            password: String
    ): CredentialRepresentation {
        val credentialRepresentation = CredentialRepresentation()
        credentialRepresentation.isTemporary = false
        credentialRepresentation.type = CredentialRepresentation.PASSWORD
        credentialRepresentation.value = password
        return credentialRepresentation
    }

    private fun prepareUserRepresentation(
            userDto: PersonDto,
            credentialRepresentation: CredentialRepresentation,
            groupName: String
    ): UserRepresentation {
        val userRepresentation = UserRepresentation()
        userRepresentation.username = userDto.username
        userRepresentation.email = userDto.email
        userRepresentation.isEnabled = true
        userRepresentation.isEmailVerified = false
        userRepresentation.firstName = userDto.name
        userRepresentation.lastName = userDto.lastName + " " + userDto.motherLastName
        userRepresentation.credentials = listOf(credentialRepresentation)
        userRepresentation.groups = listOf(groupName)
        return userRepresentation
    }

    fun getSemester(currentDate: LocalDate): String =
            when (currentDate.monthValue) {
                in 2..6 -> "I-${currentDate.year}"
                in 8..11 -> "II-${currentDate.year}"
                else -> throw IllegalArgumentException("Invalid month for semester determination")
            }}

