package com.users.msusers.bl

import com.users.msusers.dao.UserRepository
import com.users.msusers.dto.PersonDto
import com.users.msusers.dto.RelatorDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommitteeBl constructor(
    @Autowired private val userRepository: UserRepository
){

    /**
     * This method is used to find the details of a committee member by their keycloak id
     */
    fun findCommitteeMemberDetails(kcId: String): RelatorDto{
        val user = userRepository.findByIdKc(kcId)
        return RelatorDto(user.idKc, user.name, user.lastName, user.motherLastName,user.email,
            user.phoneNumber, user.group)
    }

}