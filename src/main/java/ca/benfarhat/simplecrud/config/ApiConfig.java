package ca.benfarhat.simplecrud.config;

import static ca.benfarhat.simplecrud.ApiConstant.CST_CONTEXT_PATH;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApiConfig: Configuration de l'API 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@Configuration
public class ApiConfig {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
	  webServerFactoryCustomizer() {
	    return factory -> factory.setContextPath(CST_CONTEXT_PATH);
	}
	
	
}
