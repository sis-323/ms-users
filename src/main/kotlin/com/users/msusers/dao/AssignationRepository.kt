package com.users.msusers.dao

import com.users.msusers.entity.Assignation
import com.users.msusers.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AssignationRepository : JpaRepository<Assignation, Long> {

    fun findByStudentId(studentId: Person): Assignation

    fun findAllByTutorId(tutorId: Person): List<Assignation>

    fun existsByStudentId(studentId: Person): Boolean

    fun findByStudentIdAndRelatorId(studentId: Person, relatorId: Person): Assignation

    fun findAllByRelatorId(relatorId: Person): List<Assignation>

    fun findByStudentIdIdKc(studentId: String): Assignation

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Assignation a " +
            "WHERE a.studentId.idKc = :studentId and a.tutorId IS NOT NULL")
    fun tutorExistsByStudentIdIdKc(@Param("studentId") studentId: String): Boolean

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Assignation a WHERE a.studentId.idKc = :studentId and a.relatorId IS NOT NULL")
    fun relatorExistsByStudentIdIdKc(@Param("studentId") studentId: String): Boolean

}