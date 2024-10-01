package com.fashion.api.employee.ui.dto.response

import com.fashion.api.common.response.Meta
import com.fashion.api.employee.domain.entity.Employee

data class EmployeeResponse(
    val meta: Meta?,
    val data: List<Employee>
)
