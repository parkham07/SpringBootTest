package kr.co.parkham.config.handler;

import kr.co.parkham.dto.UserRole;
import kr.co.parkham.dto.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());

		if(accessDeniedException instanceof AccessDeniedException) {
			var authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null) {
				Collection<? extends GrantedAuthority> roleTypes = authentication.getAuthorities();

				if(!roleTypes.isEmpty()) {
					request.setAttribute("msg", "접근권한 없는 사용자입니다.");

					if (roleTypes.contains(UserRole.RoleType.ROLE_VIEW)) {
						request.setAttribute("nextPage", "/");
					}
				} else {
					request.setAttribute("msg", "로그인이 필요합니다.");
					request.setAttribute("nextPage", "/login");
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					SecurityContextHolder.clearContext();
				}
			}
		} else {
			log.error(accessDeniedException.getClass().getCanonicalName());
		}

		request.getRequestDispatcher("/err/denied-page").forward(request, response);
	}
}
