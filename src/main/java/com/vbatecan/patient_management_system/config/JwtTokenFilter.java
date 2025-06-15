package com.vbatecan.patient_management_system.config;

import com.vbatecan.patient_management_system.model.entities.UserAccount;
import com.vbatecan.patient_management_system.security.JwtService;
import com.vbatecan.patient_management_system.service.interfaces.UserAccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNullApi;

import java.io.IOException;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final UserAccountService userAccountService;
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		getToken(request).ifPresent(token -> {
			if (  !jwtService.isTokenExpired(token) && SecurityContextHolder.getContext().getAuthentication() == null ) {
				String username = jwtService.getUsername(token);
				Optional<UserAccount> userAccount = userAccountService.findByUsername(username);

				// User account is not found in the database so we clear the security context.
				if ( userAccount.isEmpty() ) {
					SecurityContextHolder.clearContext();
					return;
				}
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAccount.get().toDTO(), null, userAccount.get().getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		});
		filterChain.doFilter(request, response);
	}

	private Optional<String> getToken(HttpServletRequest request) {
		// Get token from cookie
		Cookie[] cookies = request.getCookies();
		if ( cookies != null ) {
			for ( Cookie cookie : cookies ) {
				if ( "token".equals(cookie.getName()) ) {
					return Optional.of(cookie.getValue());
				}
			}
		}

		// Check if token is still null, we proceed checking Authorization header
		if ( request.getHeader("Authorization") != null ) {
			String token = request.getHeader("Authorization");
			return Optional.of(token.substring(7));
		}

		return Optional.empty();
	}
}
