package com.fashion.api.common.enums

enum class ErrorCode(
    val code: String,
    val message: String,
) {
    // 직원 관련 에러 코드
    EMPLOYEE_0001("EMPLOYEE_0001", "지원하지 않는 파일 형식입니다.(csv/json 만 허용)"),
    EMPLOYEE_0002("EMPLOYEE_0002", "파일 처리 중 오류가 발생했습니다.")
    ;

    companion object {
        fun fromCode(code: String): ErrorCode? = entries.find { it.code == code }
    }
}