package com.users.msusers.integration

import com.users.msusers.dto.PersonDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.users.msusers.bl.UserBl
import com.users.msusers.exception.UserAlreadyExistsException
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

@SpringBootTest

class CreateUserTest {

    @Autowired
    private lateinit var userBl: UserBl

    @Test
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
        userBl.createUser(personDto, "students")
    }

    @Test
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
        assertThrows<UserAlreadyExistsException> {
            userBl.createUser(personDto, "students")
        }
    }

    @Test
    fun getStudentsListTest() {
        val students = userBl.findStudents()
        assert(students.isNotEmpty())
        assert(students.size > 1)

    }

    @Test
    fun personDetailsTest() {
        val student = userBl.findUserDetailsByKcId("f54676b1-4db3-45fd-85f7-665438b51483")
        assert(student != null)
        assertNotNull(student.name)
        assertNotNull(student.lastName)
        assertNotNull(student.motherLastName)
        assertNotNull(student.email)
        assertNotNull(student.phoneNumber)

    }
}