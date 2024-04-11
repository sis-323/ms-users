package com.users.msusers.bl

import com.users.msusers.dao.UserRepository
import org.keycloak.admin.client.Keycloak
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserBl @Autowired constructor(
        private val keycloak: Keycloak,
        private val userRepository: UserRepository
) {
    fun createUser(name: String){

        println("Creating user: $name")
    }
}
