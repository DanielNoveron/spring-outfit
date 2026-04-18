package com.persona.superation.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig extends CorsFilter{

	 public CorsConfig() {
	        super(configurationSource());
	    }

	    private static UrlBasedCorsConfigurationSource configurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.addAllowedOrigin("http://localhost:5173/"); 
	        config.addAllowedMethod("*"); 
	        config.addAllowedHeader("*");

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);

	        return source;
	    }
}
