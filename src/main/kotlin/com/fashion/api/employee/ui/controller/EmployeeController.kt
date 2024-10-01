package com.fashion.api.employee.ui.controller

import com.fashion.api.employee.application.service.EmployeeCommandService
import com.fashion.api.employee.application.service.EmployeeQueryService
import com.fashion.api.employee.ui.dto.request.EmployeeRequest
import com.fashion.api.employee.ui.dto.response.EmployeeResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "직원 api", description = "employee api")
@RestController
@RequestMapping("/api/employee")
class EmployeeController(
    private val employeeCommandService: EmployeeCommandService,
    private val employeeQueryService: EmployeeQueryService,
) {
    @Operation(summary = "직원 조회", description = "직원 정보 조회(페이징)")
    @GetMapping
    fun getEmployees(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<EmployeeResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val employeeResponse = employeeQueryService.getEmployees(pageable)
        return ResponseEntity.ok(employeeResponse)
    }

    @Operation(summary = "직원 상세 조회", description = "이름으로 직원 정보 조회")
    @GetMapping("/{name}")
    fun getEmployeeByName(@PathVariable name: String): ResponseEntity<EmployeeResponse> {
        val employeeResponse = employeeQueryService.getEmployeeByName(name)
        return if (employeeResponse != null) {
            ResponseEntity.ok(employeeResponse)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "직원 정보 저장", description = "csvFile or json 형식 받아서 직원 정보 저장(postman or front 앱단에서 테스트 가능)")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addEmployees(
        @RequestPart(required = false) employeeFile: MultipartFile?,
        @RequestPart(required = false) @Valid employeeRequestBody: List<EmployeeRequest>?
    ): ResponseEntity<String> {

        // 파일이 있는 경우 파일 처리
        if (employeeFile != null) {
            return employeeCommandService.saveEmployeesFromFile(employeeFile)
        }

        // RequestBody로 데이터가 들어온 경우
        if (!employeeRequestBody.isNullOrEmpty()) {
            employeeCommandService.saveEmployeesFromBody(employeeRequestBody)
            return ResponseEntity.status(HttpStatus.CREATED).body("직원 정보가 저장되었습니다.")
        }

        // 파일이나 데이터가 없는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이나 데이터를 입력해주세요.")
    }
}