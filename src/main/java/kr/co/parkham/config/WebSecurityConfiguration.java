package kr.co.parkham.config;

import kr.co.parkham.config.encoder.CustomPasswordEncoder;
import kr.co.parkham.config.filter.JwtFilter;
import kr.co.parkham.config.handler.LoginFailureHandler;
import kr.co.parkham.config.handler.LoginSuccessHandler;
import kr.co.parkham.config.handler.WebAccessDeniedHandler;
import kr.co.parkham.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final SecurityUserService securityUserService;
	private final WebAccessDeniedHandler webAccessDeniedHandler;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;
	private final JwtFilter jwtFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final var LOGIN_URL = "/login";
		PortMapperImpl portMapper = new PortMapperImpl();
		portMapper.setPortMappings(Collections.singletonMap("80","443"));

		http.authorizeRequests()
				.antMatchers("/", LOGIN_URL, "/logout", "/users/**", "/authenticate").permitAll()
				.antMatchers("/foundation/**").hasAnyRole("ADMIN", "VIEW")
				.antMatchers("/**").hasAnyRole("ADMIN", "VIEW")
				.anyRequest().authenticated()
				.and().cors()
				.and()
					.formLogin()
						.loginPage(LOGIN_URL)
						.usernameParameter("username")
						.passwordParameter("password")
						.successHandler(loginSuccessHandler)
						.failureHandler(loginFailureHandler)
				.and()
					.logout()
						.logoutSuccessUrl(LOGIN_URL)
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID", "")
						.permitAll()
				.and()
					.exceptionHandling().accessDeniedHandler(webAccessDeniedHandler);

		http.csrf().requireCsrfProtectionMatcher(new CsrfRequireMatcher())
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		var daoAuthenticationProvider = new DaoAuthenticationProvider();

		daoAuthenticationProvider.setUserDetailsService(securityUserService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//		daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // ID, PASSWORD 별 에러 메세지 구분 옵션

		return daoAuthenticationProvider;
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();

		defaultCookieSerializer.setUseSecureCookie(true);
		defaultCookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		defaultCookieSerializer.setSameSite("None");

		return defaultCookieSerializer;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", "Accept-Encoding", "Accept-Language", "Host", "Referer", "Connection", "User-Agent", "authorization", "sw-useragent", "sw-version"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "X-Total-Count", "Link"));

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

		return urlBasedCorsConfigurationSource;
	}

	static class CsrfRequireMatcher implements RequestMatcher {
		private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|POST|DELETE|PUT|OPTIONS)$");

		@Override
		public boolean matches(HttpServletRequest request) {
			if (ALLOWED_METHODS.matcher(request.getMethod()).matches()) {
				return false;
			}

			final String userAgent = request.getHeader("User-Agent");

			return !(userAgent != null && userAgent.contains("HTTPie"));
		}
	}
}
