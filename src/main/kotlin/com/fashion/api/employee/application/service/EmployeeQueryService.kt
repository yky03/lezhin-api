package com.fashion.api.employee.application.service

import com.fashion.api.common.response.Meta
import com.fashion.api.employee.domain.entity.Employee
import com.fashion.api.employee.domain.repository.EmployeeRepository
import com.fashion.api.employee.ui.dto.response.EmployeeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class EmployeeQueryService(
    private val employeeRepository: EmployeeRepository,
) {
    fun getEmployees(pageable: Pageable): EmployeeResponse {
        val employeePage: Page<Employee> = employeeRepository.findAll(pageable)
        val isExistsEmployee = employeePage.totalPages > 0

        val meta = Meta(
            currentPage = employeePage.number + 1,
            totalPageCount = employeePage.totalPages,
            pageSize = employeePage.size,
            lastPage = if (isExistsEmployee) employeePage.totalPages else 0
        )

        return EmployeeResponse(
            meta = if (isExistsEmployee) meta else null,
            data = employeePage.content
        )
    }

    fun getEmployeeByName(name: String): EmployeeResponse? {
        val employee = employeeRepository.findByName(name)

        return employee?.let {
            EmployeeResponse(
                meta = null,
                data = listOf(it)
            )
        }
    }
}