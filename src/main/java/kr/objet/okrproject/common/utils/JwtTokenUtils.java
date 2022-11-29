package kr.objet.okrproject.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenUtils {

	public static String getUsername(String token, String key) {
		return extractClaims(token, key).get("username", String.class);
	}

	public static boolean isExpired(String token, String key) {
		Date expiration = extractClaims(token, key).getExpiration();
		return expiration.before(new Date());
	}

	private static Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}

	private static String generateToken(String username, String key, Long expiredTimMs) {
		Claims claims = Jwts.claims();
		claims.put("username", username);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredTimMs))
			.signWith(getKey(key), SignatureAlgorithm.HS256)
			.compact();
	}

	private static Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
