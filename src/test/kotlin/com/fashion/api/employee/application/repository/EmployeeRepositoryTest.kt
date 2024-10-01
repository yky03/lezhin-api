package com.fashion.api.employee.application.repository

import com.fashion.api.employee.domain.entity.Employee
import com.fashion.api.employee.domain.repository.EmployeeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate

@DataJpaTest
class EmployeeRepositoryTest @Autowired constructor(
    private val employeeRepository: EmployeeRepository
) {

    @Test
    fun `이름 저장 후 이름으로 조회`() {
        val employee = Employee(
            id = 1L,
            name = "양기열",
            email = "ykycome00@gmail.com",
            tel = "010-1234-5678",
            joined = LocalDate.parse("2024-09-29")
        )
        employeeRepository.save(employee)

        val foundEmployee = employeeRepository.findByName("양기열")

        assertEquals(employee.id, foundEmployee?.id)
        assertEquals(employee.name, foundEmployee?.name)
    }

    @Test
    fun `존재하지 않는 이름으로 조회`() {
        val foundEmployee = employeeRepository.findByName("존재하지 않는 이름")
        assertNull(foundEmployee)
    }
}