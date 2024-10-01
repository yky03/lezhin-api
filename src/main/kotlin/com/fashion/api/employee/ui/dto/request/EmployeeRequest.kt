package com.fashion.api.employee.ui.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class EmployeeRequest @JsonCreator constructor(
    @field:NotBlank(message = "이름은 필수값입니다.")
    @Schema(example = "양기열", description = "직원 이름")
    @JsonProperty("name")
    val name: String,

    @field:NotBlank(message = "이메일은 필수값입니다.")
    @field:Email(message = "유효한 이메일 주소여야 합니다.")
    @Schema(example = "ykycome00@gmail.com", description = "직원의 이메일 주소")
    @JsonProperty("email")
    val email: String,

    @field:NotBlank(message = "휴대폰 번호는 필수값입니다.")
    @field:Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 휴대폰 번호여야 합니다.(ex: 010-1234-5678)")
    @Schema(example = "010-1234-5678", description = "직원 휴대폰 번호")
    @JsonProperty("tel")
    val tel: String,

    @field:NotNull(message = "입사일자는 필수값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(example = "2024-09-29", description = "직원의 입사일자(ex: yyyy-MM-DD)")
    @JsonProperty("joined")
    val joined: LocalDate
)