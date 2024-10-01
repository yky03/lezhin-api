package com.fashion.api.aop.aspect

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class LoggingAspect {
    private val logger = KotlinLogging.logger {}

    // @Around("execution(* com.fashion.api.employee.ui.controller.*.*(..))")
    @Around("execution(* com.fashion.api..controller.*.*(..))")
    fun loggingController(joinPoint: ProceedingJoinPoint): Any {
        val methodName = joinPoint.signature.name
        val className = joinPoint.target.javaClass.simpleName
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val requestInfo = buildRequestInfo(request)

        logger.info("Request method: ${request.method} URI: ${request.requestURI}")
        logger.info("Entering method: $className.$methodName with arguments: ${joinPoint.args.joinToString()}")
        logger.info("Request details: $requestInfo")

        val result = joinPoint.proceed()
        logger.info("Exiting method: $className.$methodName with result: $result")

        return result
    }

    private fun buildRequestInfo(request: HttpServletRequest): String {
        val method = request.method
        val requestURI = request.requestURI
        val queryString = request.queryString ?: ""
        return "Method: $method, URI: $requestURI, queryString: $queryString"
    }
}

