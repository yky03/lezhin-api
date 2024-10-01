package com.fashion.api.employee.application.service

import com.fashion.api.common.enums.ErrorCode
import com.fashion.api.employee.domain.repository.EmployeeRepository
import com.fashion.api.employee.ui.dto.request.EmployeeRequest
import com.fashion.api.exception.EmployeeException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.opencsv.CSVWriter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate

class EmployeeCommandServiceTest {

    private lateinit var employeeCommandService: EmployeeCommandService
    private lateinit var employeeRepository: EmployeeRepository
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository::class.java)
        objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        employeeCommandService = EmployeeCommandService(employeeRepository, objectMapper)
    }

    @Test
    fun `CSV 파일로 직원 저장 성공`() {
        // Given
        val csvContent = generateCsvContent(
            listOf(
                listOf("양기열", "ykycome00@gmail.com", "010-1234-5678", "2024.09.29")
            )
        )

        val csvFile = MockMultipartFile("employeeFile", "employees.csv", "text/csv", csvContent)

        // When
        val response: ResponseEntity<String> = employeeCommandService.saveEmployeesFromFile(csvFile)

        // Then
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("직원 정보 저장 성공.", response.body)
    }

    @Test
    fun `JSON 파일로 직원 저장 성공`() {
        // Given
        val employeeRequestList = listOf(
            EmployeeRequest(
                name = "양기열",
                email = "ykycome00@gmail.com",
                tel = "010-1234-5678",
                joined = LocalDate.parse("2024-09-29")
            )
        )

        val jsonFile = MockMultipartFile(
            "employeeFile",
            "employees.json",
            "application/json",
            objectMapper.writeValueAsBytes(employeeRequestList)
        )

        // When
        val response: ResponseEntity<String> = employeeCommandService.saveEmployeesFromFile(jsonFile)

        // Then
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("직원 정보 저장 성공.", response.body)
    }

    @Test
    fun `CSV 파일 파싱 실패 시 예외 발생`() {
        // Given
        val invalidCsvContent = "잘못된 데이터 형식".toByteArray()
        val invalidCsvFile = MockMultipartFile("employeeFile", "invalid.xlsx", "text/plain", invalidCsvContent)

        // When & Then
        val exception = assertThrows(EmployeeException::class.java) {
            employeeCommandService.saveEmployeesFromFile(invalidCsvFile)
        }

        assertEquals(ErrorCode.EMPLOYEE_0001.code, exception.code)
    }

    @Test
    fun `JSON 파일 파싱 실패 시 예외 발생`() {
        // Given
        val invalidJsonContent = "잘못된 데이터 형식".toByteArray()
        val invalidJsonFile = MockMultipartFile("employeeFile", "invalid.json", "application/json", invalidJsonContent)

        // When & Then
        val exception = assertThrows(EmployeeException::class.java) {
            employeeCommandService.saveEmployeesFromFile(invalidJsonFile)
        }

        assertEquals(ErrorCode.EMPLOYEE_0002.code, exception.code)
    }

    private fun generateCsvContent(rows: List<List<String>>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val writer = CSVWriter(OutputStreamWriter(outputStream))

        rows.forEach { row ->
            writer.writeNext(row.toTypedArray())
        }
        writer.flush()
        return outputStream.toByteArray()
    }
}