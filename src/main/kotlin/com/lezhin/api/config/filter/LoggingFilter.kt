package com.lezhin.api.config.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class LoggingFilter : Filter {

    private val logger = KotlinLogging.logger {}

    override fun init(filterConfig: FilterConfig) {
        // 필터 초기화 시 필요한 설정이 있으면 추가
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        // 요청 정보 로깅
        val requestInfo = buildRequestInfo(httpRequest)
        logger.info("Incoming Request: $requestInfo")

        // 필터 체인 계속
        chain.doFilter(request, response)

        // 응답 정보 로깅
        val status = httpResponse.status
        logger.info("Outgoing Response: Status $status")
    }

    override fun destroy() {
        // 필터 종료 시 필요한 설정이 있으면 추가
    }

    private fun buildRequestInfo(request: HttpServletRequest): String {
        val method = request.method
        val requestURI = request.requestURI
        val queryString = request.queryString ?: ""
        val remoteAddr = request.remoteAddr
        return "Method: $method, URI: $requestURI, QueryString: $queryString, RemoteAddr: $remoteAddr"
    }
}