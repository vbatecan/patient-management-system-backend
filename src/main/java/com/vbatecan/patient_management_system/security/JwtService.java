package com.vbatecan.patient_management_system.security;


import com.vbatecan.patient_management_system.model.entities.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.exp-time}")
	private Long jwtExpireTime;

	public String generateToken(UserDetails userDetails) throws ClassCastException{
		UserAccount userAccount = (UserAccount) userDetails;
		Map<String, Object> claims = Map.of("role", userAccount.getRole().name());
		return buildToken(userDetails, claims);
	}

	private String buildToken(
		UserDetails userDetails,
		Map<String, ?> claims
	) {
		return Jwts.builder()
			.claims(claims)
			.subject(userDetails.getUsername())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + jwtExpireTime))
			.signWith(getKey(), Jwts.SIG.HS256)
			.compact();
	}

	private String getUsername(String token) {
		Claims claims = claims(token);
		return claims.getSubject();
	}

	private Claims claims(String token) {
		return Jwts
			.parser()
			.verifyWith(getKey())
			.decryptWith(getKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private SecretKey getKey() {
		byte[] keyBytes = jwtSecret.getBytes();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
	}
}
