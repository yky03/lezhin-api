package com.fashion.api.employee.application.service

import com.fashion.api.employee.domain.entity.Employee
import com.fashion.api.employee.domain.repository.EmployeeRepository
import com.fashion.api.employee.ui.dto.response.EmployeeResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDate

@SpringBootTest
class EmployeeQueryServiceTest {

    @MockBean
    private lateinit var employeeRepository: EmployeeRepository

    private lateinit var employeeList: List<Employee>

    @BeforeEach
    fun setUp() {
        employeeList = listOf(
            Employee(
                id = 1,
                name = "양기열",
                email = "ykycome00@gmail.com",
                tel = "010-1234-5678",
                joined = LocalDate.parse("2024-09-29")
            )
        )
    }

    @Test
    fun `직원 목록 조회 테스트 - 페이징`() {
        val page = 1
        val pageSize = 10
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val employeePage: Page<Employee> = PageImpl(employeeList, pageable, 1)

        Mockito.`when`(employeeRepository.findAll(pageable))
            .thenReturn(employeePage)

        val employeeQueryService = EmployeeQueryService(employeeRepository)

        val result: EmployeeResponse = employeeQueryService.getEmployees(pageable)

        assertEquals(1, result.meta?.currentPage)
        assertEquals(1, result.meta?.totalPageCount)
        assertEquals(10, result.meta?.pageSize)
        assertEquals(1, result.meta?.lastPage)
        assertEquals(1, result.data.size)
        assertEquals("양기열", result.data[0].name)
    }

    @Test
    fun `직원 이름으로 조회 테스트`() {
        val employee = employeeList[0]

        Mockito.`when`(employeeRepository.findByName("양기열"))
            .thenReturn(employee)

        val employeeQueryService = EmployeeQueryService(employeeRepository)

        val result: EmployeeResponse? = employeeQueryService.getEmployeeByName("양기열")

        assertEquals(1, result?.data?.size)
        assertEquals("양기열", result?.data?.get(0)?.name)
        assertEquals("ykycome00@gmail.com", result?.data?.get(0)?.email)
    }
}