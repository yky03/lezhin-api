package com.lezhin.api.config.web

import com.lezhin.api.config.interceptor.AuthInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.util.UrlPathHelper

@Configuration
@EnableWebMvc
class WebConfiguration(
    private val authInterceptor: AuthInterceptor
) : WebMvcConfigurer {

    @Value("\${cors.allowed-origins:*}")
    private lateinit var allowedOrigins: String

    @Value("\${cors.allowed-methods:PUT,GET,POST,HEAD,DELETE}")
    private lateinit var allowedMethods: String

    private fun getAllowedMethods() = allowedMethods.split(",").toTypedArray()

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        val urlPathHelper = UrlPathHelper()
        urlPathHelper.isUrlDecode = false
        configurer.setUrlPathHelper(urlPathHelper)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(*getAllowedMethods())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/v1/comics/**") // 인증이 필요한 경로 설정
            .excludePathPatterns("/api/v1/members/**") // 인증이 필요 없는 경로 설정
    }
}
