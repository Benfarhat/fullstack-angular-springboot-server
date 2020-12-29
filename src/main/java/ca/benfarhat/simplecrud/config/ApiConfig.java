package ca.benfarhat.simplecrud.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.benfarhat.simplecrud.ApiConstants;

@Configuration
public class ApiConfig {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
	  webServerFactoryCustomizer() {
	    return factory -> factory.setContextPath(ApiConstants.ROOT_CONTEXT_PATH);
	}
	
}
