package com.fashion.api.common.util

object CommonUtils {
    fun formatPhoneNumber(rawNumber: String): String {
        return if (rawNumber.length == 11) {
            "${rawNumber.substring(0, 3)}-${rawNumber.substring(3, 7)}-${rawNumber.substring(7)}"
        } else {
            rawNumber
        }
    }
}

