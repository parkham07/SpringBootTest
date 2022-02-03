package kr.co.parkham.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUser extends org.springframework.security.core.userdetails.User implements Serializable {
	private static final long serialVersionUID = 842398792128548582L;

	User user;

	public SecurityUser(User user) {
		super(user.getEmp(), user.getPassword(), authorities(user.getRoleTypes()));

		this.user = user;
	}

	public User getUser() {
		return user;
	}

	private static Collection<? extends GrantedAuthority> authorities(Set<UserRole.RoleType> types) {
		return types.stream()
				.map(r -> new SimpleGrantedAuthority(r.name()))
				.collect(Collectors.toSet());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		SecurityUser that = (SecurityUser) o;
		return Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), user);
	}
}
