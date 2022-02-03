package kr.co.parkham.config.util;

import io.jsonwebtoken.*;
import kr.co.parkham.commons.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtUtil {

	private static String SECRET = "test";
	private static final long TIME_TO_LIVE = 10 * 60 * 60 * 1000;

	@PostConstruct
	private void init() {
		SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
	}

	// Jwt 토큰에서 회원 구별 정보 추출 - 회원 ID
	public String getUserPk(String jwtToken) throws Exception {
		String result = "";

		try {
			if(!isTokenExpired(jwtToken)) {
				result = extractUserPk(jwtToken);
			}
		} catch (Exception e) {
			log.error(Common.getPrintStackTrace(e));
		}

		return result;
	}

	private String extractUserPk(String token) {
		return extractClaim(token, Claims::getId);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(Authentication authentication) {
		return createToken(authentication);
	}

	private String createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Claims claims = Jwts.claims().setSubject(authentication.getName());
		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.claim("auth", authorities)
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + TIME_TO_LIVE))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
	}

	public Boolean validateToken(String token) {
//		if(token == null) {
//			return false;
//		}
//
//		final String username = extractUsername(token);
//
//		return (username.equals(authentication.getPrincipal()) && !isTokenExpired(token));
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}

		return false;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody();

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get("auth").toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
}
