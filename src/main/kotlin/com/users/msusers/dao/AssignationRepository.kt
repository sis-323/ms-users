package com.users.msusers.dao

import com.users.msusers.entity.Assignation
import com.users.msusers.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignationRepository : JpaRepository<Assignation, Long> {

    fun findByStudentId(studentId: Person): Assignation

    fun findAllByTutorId(tutorId: Person): List<Assignation>

}