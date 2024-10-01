package com.fashion.api.config.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.util.UrlPathHelper

@Configuration
@EnableWebMvc
class WebConfiguration : WebMvcConfigurer {

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
}
