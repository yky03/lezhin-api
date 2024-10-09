package com.lezhin.api.config.interceptor

import com.lezhin.api.common.enums.ErrorCode
import com.lezhin.api.exception.AuthException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor : HandlerInterceptor {

    private val logger = KotlinLogging.logger {}
    private val MASTER_EXTERNAL_KEY = "7725010aed35e854d819a3de7cac78aae6f054d14d9f95b3b9f5075ec956c77a"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !isValidToken(authHeader)) {
            logger.error("${ErrorCode.AUTH_0001.message} authHeader: $authHeader")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            throw AuthException(ErrorCode.AUTH_0001)
        }

        return true
    }

    private fun isValidToken(token: String): Boolean {
        return token == MASTER_EXTERNAL_KEY
    }
}
