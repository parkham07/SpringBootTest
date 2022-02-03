package kr.co.parkham.controller;

import kr.co.parkham.config.annotations.AuthUser;
import kr.co.parkham.dto.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

	@GetMapping(value = "/")
	public String index(@AuthUser User user) {
		if(user == null) {
			return "redirect:/login";
		}

		return "index";
	}

	@GetMapping(value = "/login")
	public String login(@AuthUser User user) {
		if(user == null) {
			return "login";
		}

		return "redirect:/";
	}

	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null && authentication.isAuthenticated()) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);

			SecurityContextHolder.clearContext();
		}

		return "redirect:/login";
	}

	@GetMapping(value = "/signup")
	public String signup(@AuthUser User user) {
		if(user != null) {
			return "redirect:/";
		}

		return "signup";
	}

	@RequestMapping(value = "/err/denied-page")
	public String accessDenied() {
		return "err/deniedPage";
	}
}