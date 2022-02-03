package kr.co.parkham.controller;

import kr.co.parkham.config.util.JwtUtil;
import kr.co.parkham.service.SecurityUserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiController {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private SecurityUserService securityUserService;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AuthRequest {
		private String userName;
		private String password;
	}

	@Builder
	@Data
	public static class AuthResponse {
		private String token;
	}

	@PostMapping("/authenticate")
	public AuthResponse authenticate(@RequestBody AuthRequest authRequest, HttpServletRequest request) throws Exception {
		UserDetails userDetails = securityUserService.loadUserByUsername(authRequest.getUserName());

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		return AuthResponse.builder()
				.token(jwtUtil.generateToken(usernamePasswordAuthenticationToken))
				.build();
	}

	@PostMapping("/authenticateTest")
	public String authenticateTest() throws Exception {
		return "성공";
	}
}
