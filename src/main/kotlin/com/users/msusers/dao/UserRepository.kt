package com.users.msusers.dao

import com.users.msusers.entity.Person
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
@Repository
interface UserRepository: JpaRepository<Person, Long>{

    fun findByEmail(email: String): Person

}