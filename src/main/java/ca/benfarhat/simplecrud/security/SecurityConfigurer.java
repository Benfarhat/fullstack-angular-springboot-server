package ca.benfarhat.simplecrud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ca.benfarhat.simplecrud.ApiConstant;
import ca.benfarhat.simplecrud.filter.JwtRequestFilter;
import ca.benfarhat.simplecrud.service.AppUserDetailsService;

/**
 * 
 * SecurityConfigurer: Configuration de la sécurité 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-02
 * @version 1.0.0
 *
 */

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(noPasswordEncoder());
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}	
	
    @SuppressWarnings("deprecation")
	@Bean
    public static NoOpPasswordEncoder noPasswordEncoder() {
       return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests().antMatchers(
				ApiConstant.CST_AUTHENTICATE_CTX,
				// Ajout des urls swagger
				"/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**").permitAll()
		.anyRequest().authenticated()
		// a présent on indique le filtre a utiliser pour extraire le jeton et l'inclure dans le contexte utilisateur et de ne pas maintenir de session
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
    
    
}
