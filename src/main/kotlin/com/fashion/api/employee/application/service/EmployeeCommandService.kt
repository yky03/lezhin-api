package com.fashion.api.employee.application.service

import com.fashion.api.common.enums.ErrorCode
import com.fashion.api.common.util.CommonUtils
import com.fashion.api.employee.domain.factory.EmployeeFactory
import com.fashion.api.employee.domain.repository.EmployeeRepository
import com.fashion.api.employee.ui.dto.request.EmployeeRequest
import com.fashion.api.exception.EmployeeException
import com.fasterxml.jackson.databind.ObjectMapper
import com.opencsv.CSVReader
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EmployeeCommandService(
    private val employeeRepository: EmployeeRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger {}

    companion object {
        private const val SAVE_SUCCESS_MESSAGE = "직원 정보 저장 성공."
    }

    fun saveEmployeesFromBody(employeeRequestList: List<EmployeeRequest>) {
        val employees = employeeRequestList.map { EmployeeFactory.createFromRequest(it) }
        employeeRepository.saveAll(employees)
    }

    fun saveEmployeesFromFile(employeeFile: MultipartFile): ResponseEntity<String> =
        when (employeeFile.contentType) {
            "text/csv" -> saveByCsvFile(employeeFile)
            "application/json" -> saveByJsonFile(employeeFile)
            else -> throw EmployeeException(ErrorCode.EMPLOYEE_0001)
        }


    private fun saveByCsvFile(file: MultipartFile): ResponseEntity<String> {
        return try {
            val csvReader = CSVReader(InputStreamReader(file.inputStream))
            csvReader.readNext()

            val employees = csvReader.readAll().map { line ->
                EmployeeRequest(
                    name = line[0].trim(),
                    email = line[1].trim(),
                    tel = CommonUtils.formatPhoneNumber(line[2].trim()),
                    joined = LocalDate.parse(line[3].trim(), DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                )
            }

            this.saveEmployeesFromBody(employees)
            ResponseEntity.status(HttpStatus.CREATED).body(SAVE_SUCCESS_MESSAGE)
        } catch (e: Exception) {
            logger.error(e.message)
            throw EmployeeException(ErrorCode.EMPLOYEE_0002)
        }
    }


    private fun saveByJsonFile(file: MultipartFile): ResponseEntity<String> {
        return try {
            val employees: List<EmployeeRequest> = objectMapper.readValue(
                file.inputStream,
                objectMapper.typeFactory.constructCollectionType(List::class.java, EmployeeRequest::class.java)
            )
            saveEmployeesFromBody(employees)
            ResponseEntity.status(HttpStatus.CREATED).body(SAVE_SUCCESS_MESSAGE)
        } catch (e: Exception) {
            logger.error(e.message)
            throw EmployeeException(ErrorCode.EMPLOYEE_0002)
        }
    }
}