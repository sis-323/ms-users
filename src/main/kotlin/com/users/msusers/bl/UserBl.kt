package com.users.msusers.bl

import com.users.msusers.dao.ModalityRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.RelatorDto
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

    fun findStudents(): List<UserDto>{
        val students = userRepository.findStudents()
        return students.map {
            UserDto(
                    it.modality!!.modality,
                    it.name,
                    it.lastName,
                    it.motherLastName,
                    it.email,
                    it.phoneNumber,
            )
        }
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

