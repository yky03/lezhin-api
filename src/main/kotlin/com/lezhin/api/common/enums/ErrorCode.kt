package com.lezhin.api.common.enums

enum class ErrorCode(
    val code: String,
    val message: String,
) {
    AUTH_0001("AUTH_0001", "인증키가 유효하지 않습니다."),
    ;

    companion object {
        fun fromCode(code: String): ErrorCode? = entries.find { it.code == code }
    }
}