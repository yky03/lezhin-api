package com.fashion.api.employee.domain.factory

import com.fashion.api.employee.domain.entity.Employee
import com.fashion.api.employee.ui.dto.request.EmployeeRequest
import java.time.LocalDate

object EmployeeFactory {

    fun createFromRequest(employeeRequest: EmployeeRequest): Employee {
        return Employee(
            name = employeeRequest.name,
            email = employeeRequest.email,
            tel = employeeRequest.tel,
            joined = employeeRequest.joined
        )
    }

    fun createFromCsv(csvData: List<String>): Employee {
        return Employee(
            name = csvData[0],
            email = csvData[1],
            tel = csvData[2],
            joined = LocalDate.parse(csvData[3])
        )
    }
}