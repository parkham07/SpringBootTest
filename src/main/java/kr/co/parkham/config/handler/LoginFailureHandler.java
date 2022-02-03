package kr.co.parkham.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String errorMsg = "기본 에러 메세지";

		if(exception instanceof BadCredentialsException){
			errorMsg = "";
		} else if(exception instanceof UsernameNotFoundException) {
			errorMsg = "유저 없음";
		} else if(exception instanceof InsufficientAuthenticationException){
			errorMsg = "키 에러";
		}

		setDefaultFailureUrl("/login?errorMsg=" + errorMsg);

		super.onAuthenticationFailure(request, response, exception);
	}
}
