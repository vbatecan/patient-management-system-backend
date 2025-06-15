package com.vbatecan.patient_management_system.security;

import com.vbatecan.patient_management_system.config.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

		return security
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(requests -> {
					requests.anyRequest().permitAll();
				})
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}
}
