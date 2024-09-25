package com.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private static final String ALLOW_METHOD_NAMES = "GET,HEAD,POST,DELETE,PATCH,PUT,OPTIONS";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods(ALLOW_METHOD_NAMES.split(","))
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
