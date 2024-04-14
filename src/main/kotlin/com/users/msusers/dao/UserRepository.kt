package com.users.msusers.dao

import com.users.msusers.entity.Person
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Repository
interface UserRepository: JpaRepository<Person, Long>{

    fun findByEmail(email: String): Person

    @Query("SELECT p FROM Person p WHERE p.group = 'students'")
    fun findStudents(): List<Person>

}