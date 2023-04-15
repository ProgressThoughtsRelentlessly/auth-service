package com.pthore.service.auth.configs;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.pthore.service.auth.utils.AppConstants;

@EnableWebSecurity(debug = true)
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter{

	
	
/*********************** HTTP BASIC AUTHENTICATION CONFIG ********************************************/  
    @Bean 
    public UserDetailsManager userDetailsManager() {
    	return new CustomUserDetailsManager();
    }
    @Bean 
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
/*********************** OAuth2 Authorization *******************************************************/  	
	@Bean
	protected ClientRegistration clientRegistration() {
		return CommonOAuth2Provider.GOOGLE.getBuilder("google")
				.clientId(AppConstants.GOOGLE_CLIENT_ID)
				.clientSecret(AppConstants.GOOGLE_CLIENT_SECRET)
				.build();
	}
	
	@Bean
	protected ClientRegistrationRepository clientRepository() {
		return new InMemoryClientRegistrationRepository(clientRegistration());
	}

/*********************** OVERALL SECURITY CONFIG *********************************************/  
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) )
		.cors()
		.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization", "jwt"));  // This is allow headers to be sent in response from this server to other servers requesting service.
				config.setMaxAge(3600L); // 5mins
				return config;
			}}).and().csrf().disable()
			.authorizeRequests()
			.mvcMatchers("/loginGoogle").authenticated()
			.and().oauth2Login();
	}
}
