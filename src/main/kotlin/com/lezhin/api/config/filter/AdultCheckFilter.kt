package com.lezhin.api.config.filter

import com.lezhin.api.exception.AdultException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException

@Order(2)
@Component
class AdultCheckFilter : HttpFilter() {
    private val logger = KotlinLogging.logger {}

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val isAdult = request.getHeader("isAdult")?.toBoolean() ?: false

        if (!isAdult) {
            val errorMessage = "성인 인증이 필요합니다."
            logger.warn(errorMessage)
            // throw AdultException("ADULT_ERROR", errorMessage)
        }

        chain.doFilter(request, response)
    }
}

