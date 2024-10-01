package com.fashion.api.exception

import com.fashion.api.common.enums.ErrorCode

class EmployeeException(
    val code: String,
    message: String,
) : RuntimeException(message) {

    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.message
    )
}

