package com.users.msusers.integration

import com.users.msusers.bl.RelatorBl
import com.users.msusers.bl.TutorBl
import com.users.msusers.dto.PersonDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.users.msusers.bl.UserBl
import com.users.msusers.dao.UserRepository
import com.users.msusers.exception.UserAlreadyExistsException
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.assertThrows
import javax.ws.rs.ClientErrorException
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest

class UserBlTests {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userBl: UserBl
    @Autowired
    private lateinit var tutorBl: TutorBl
    @Autowired
    private lateinit var relatorBl: RelatorBl

    @Test
    @Order(1)
    fun createStudentsTest() {
        val personDto = PersonDto(
            1L,
            "name",
            "lastName",
            "motherLastName",
            "email@ucb.edu.bo",
            "77772222",
            "password",
            "mock_username"
        )
        assertThrows<UserAlreadyExistsException> {
            userBl.createUser(personDto, "students")
        }

    }

    @Test
    @Order(2)
    fun createTutorTest() {
        val personDto = PersonDto(
            1L,
            "tutor_test_mock",
            "lastName_test_mock",
            "motherlastname_mock",
            "tutor.mock@ucb.edu.bo",
            "99991919",
            "password",
            "mock_tutor"
        )

        userBl.createUser(personDto, "tutors")
    }


    @Test
    @Order(3)
    fun createExistingUserTest() {
        val personDto = PersonDto(
            1L,
            "name",
            "lastName",
            "motherLastName",
            "email@ucb.edu.bo",
            "77772222",
            "password",
            "mock_username"
        )
        userBl.createUser(personDto, "students")

    }

    @Test
    @Order(4)
    fun createInvalidStudentTest() {
        val personDto = PersonDto(
            1L,
            "invalid_name",
            "lastName_1",
            "motherLastName_1",
            "email1",
            "77772222",
            "password",
            "mock_username"
        )
        assertThrows<ClientErrorException> {
            userBl.createUser(personDto, "students")
        }


    }


    @Test
    @Order(5)
    fun getStudentsListTest() {
        val students = userBl.findStudents()
        for (student in students) {
            assertNotNull(student.name)
            assertNotNull(student.lastName)
            assertNotNull(student.motherLastName)
            assertNotNull(student.email)
            assertNotNull(student.phoneNumber)

        }
        assert(students.isNotEmpty())
        assert(students.size >= 1)

    }

    @Test
    @Order(6)
    fun personDetailsTest() {
        val student = userBl.findUserDetailsByKcId("47b30713-21c1-44c7-8105-a4a35b3beda0")
        println(student.toString()  )
        assertNotNull(student.name)
        assertNotNull(student.lastName)
        assertNotNull(student.motherLastName)
        assertNotNull(student.email)
        assertNotNull(student.phoneNumber)
        assertNull(student.password)

    }

    @Test
    @Order(7)
    fun assignTutorToStudentTest() {
        val tutor = userRepository.findByIdKc("adfbaf75-146f-4519-b37e-6eb56869d1d5")
        val student = userRepository.findByIdKc("47b30713-21c1-44c7-8105-a4a35b3beda0")
        tutorBl.assignTutor(student.idKc, tutor.idKc)
        val studentDetails = userBl.findStudentDetailsByKcId(student.idKc)
        assert(studentDetails.tutor == tutor.name)
        assert(studentDetails.relator == "Sin asignar")

    }

    @Test
    @Order(8)
    fun assignRelatorToStudentTest() {
        val relator = userRepository.findByIdKc("21861682-ad38-42d8-a0cd-20c9bbfa9688")
        val student = userRepository.findByIdKc("47b30713-21c1-44c7-8105-a4a35b3beda0")
        relatorBl.assignRelator(student.idKc, relator.idKc)
        val studentDetails = userBl.findStudentDetailsByKcId(student.idKc)
        assert(studentDetails.relator == relator.name)
        assertNotEquals(studentDetails.tutor, "Sin asignar")

    }
}