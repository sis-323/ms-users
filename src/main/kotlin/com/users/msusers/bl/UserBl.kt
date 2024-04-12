package com.users.msusers.bl

import com.users.msusers.dao.ModalityRepository
import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.entity.Person
import org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.ws.rs.ClientErrorException

@Service
class UserBl @Autowired constructor(
        private val keycloak: Keycloak,
        private val userRepository: UserRepository,
        private val modalityRepository: ModalityRepository
) {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(UserBl::class.java.name)
    }

    @Value("\${keycloak.auth-server-url}")
    private val authUrl: String? = null

    @Value("\${keycloak.credentials.realm}")
    private val realm: String? = null

    @Value("\${keycloak.credentials.secret}")
    private val secretKey: String? = null



    fun createUser(personDto: PersonDto, groupName: String){


        val passwordRepresentation = preparePasswordRepresentation(personDto.password!!)
        val userRepresentation = prepareUserRepresentation(personDto, passwordRepresentation, groupName)

        val response = keycloak.realm(realm).users().create(userRepresentation)

        if (response.status != 201) {
            throw ClientErrorException(response)
        }
        val userUuid = response.location.path.split("/").last()

        if(groupName == "students"){
            val student = Person()
            val modality = this.modalityRepository.findByIdModality(personDto.modalityId!!)
            student.modality = modality
            student.name = personDto.name!!
            student.lastName = personDto.lastName!!
            student.motherLastName = personDto.motherLastName!!
            student.email = personDto.email!!
            student.phoneNumber = personDto.phoneNumber!!
            student.group = groupName
            student.status = true
            student.idKc = userUuid
            userRepository.save(student)
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
}

