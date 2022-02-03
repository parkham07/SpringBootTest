package kr.co.parkham.service;

import kr.co.parkham.dto.SecurityUser;
import kr.co.parkham.dto.User;
import kr.co.parkham.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityUserService implements UserDetailsService {

	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = new User();

		user.setEmp(username);
		user.setPassword("33wFAhZVPSj+tBDyeo5t+Q==");
		user.setRole("0");

		if(user == null ) {
			throw new UsernameNotFoundException(username);
		}

		return new SecurityUser(user);
	}
}
