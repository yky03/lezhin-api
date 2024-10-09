package com.lezhin.api.exception

import com.lezhin.api.common.enums.ErrorCode

class AdultException(
    val code: String,
    message: String,
) : RuntimeException(message) {

    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.message
    )
}

