package com.users.msusers.dao

import com.users.msusers.entity.Person
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Repository
interface UserRepository: JpaRepository<Person, Long>{

    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Person
    fun findByIdKc(idKc: String): Person

    @Query("SELECT p FROM Person p WHERE p.group = 'students'")
    fun findStudents(): List<Person>

    @Query("SELECT p FROM Person p WHERE p.group = 'relators' AND p.status = true")
    fun findRelators(): List<Person>

    @Query("SELECT p FROM Person p WHERE p.group = 'tutors' OR p.group = 'relators' AND p.status = true")
    fun findCommitteeMembers(): List<Person>

    @Query("SELECT p FROM Person p WHERE p.group = 'tutors' and p.status = true")
    fun findAllTutors(): List<Person>
}