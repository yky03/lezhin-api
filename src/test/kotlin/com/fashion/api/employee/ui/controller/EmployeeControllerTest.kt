package com.fashion.api.employee.ui.controller

import LocalDateAdapter
import com.fashion.api.common.response.Meta
import com.fashion.api.employee.application.service.EmployeeCommandService
import com.fashion.api.employee.application.service.EmployeeQueryService
import com.fashion.api.employee.domain.entity.Employee
import com.fashion.api.employee.ui.dto.request.EmployeeRequest
import com.fashion.api.employee.ui.dto.response.EmployeeResponse
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var wac: WebApplicationContext

    @Mock
    private lateinit var employeeCommandService: EmployeeCommandService

    @Mock
    private lateinit var employeeQueryService: EmployeeQueryService

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @DisplayName("직원 조회 테스트")
    @Test
    fun getEmployeesTest() {
        val page = 1
        val pageSize = 10
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)

        val employees = listOf(
            Employee(
                id = 1,
                name = "양기열",
                email = "ykycome00@gmail.com",
                tel = "010-1234-5678",
                joined = LocalDate.parse("2024-09-29")
            )
        )

        val employeeResponse = EmployeeResponse(
            meta = Meta(currentPage = page, totalPageCount = 1, pageSize = pageSize, lastPage = 1),
            data = employees
        )

        Mockito.`when`(employeeQueryService.getEmployees(pageable))
            .thenReturn(employeeResponse)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee")
            .param("page", page.toString())
            .param("pageSize", pageSize.toString())
            .contentType(MediaType.APPLICATION_JSON))
    }

    @DisplayName("직원 상세 조회 테스트")
    @Test
    fun getEmployeeByNameTest() {
        val employee = Employee(
            id = 1,
            name = "양기열",
            email = "ykycome00@gmail.com",
            tel = "010-1234-5678",
            joined = LocalDate.parse("2024-09-29")
        )

        val employeeResponse = EmployeeResponse(
            meta = Meta(currentPage = 1, totalPageCount = 1, pageSize = 10, lastPage = 1),
            data = listOf(employee)
        )

        Mockito.`when`(employeeQueryService.getEmployeeByName("양기열"))
            .thenReturn(employeeResponse)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/양기열"))
    }

    @DisplayName("직원 정보 저장 테스트 - CSV 파일")
    @Test
    fun addEmployeesTestWithFile() {
        val csvFile = MockMultipartFile(
            "employeeFile",
            "employees.csv",
            "text/csv",
            "id,name,email,tel,joined\n1,양기열,ykycome00@gmail.com,010-1234-5678,2024-09-29".toByteArray()
        )

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/employee")
                .file(csvFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
    }

    @DisplayName("직원 정보 저장 테스트 - JSON 데이터")
    @Test
    fun addEmployeesTestWithJson() {
        val employeeRequestList = listOf(
            EmployeeRequest(
                name = "양기열",
                email = "ykycome00@gmail.com",
                tel = "010-1234-5678",
                joined = LocalDate.parse("2024-09-29")
            )
        )

        val jsonData = gson.toJson(employeeRequestList)

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/employee")
                .content(jsonData)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}
