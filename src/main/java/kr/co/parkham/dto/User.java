package kr.co.parkham.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class User implements Serializable {
	private static final long serialVersionUID = 7591250912282016031L;

	private String emp;
	private String password;
	private Set<UserRole> userRoles;

	public void setRole(String role) {
		userRoles = new HashSet<>();

		if(StringUtils.hasText(role)) {
			userRoles.add(new UserRole(UserRole.RoleType.parse(role)));
		}
	}

	public boolean isCheckUserRole(UserRole.RoleType roleType) {
		if(getUserRoles() == null || getUserRoles().size() <= 0) {
			return false;
		}

		return getUserRoles().stream().allMatch(r -> r.getRoleType().ordinal() <= roleType.ordinal());
	}

	public Set<UserRole.RoleType> getRoleTypes() {
		Set<UserRole.RoleType> roleTypes = new HashSet<>();

		if(userRoles != null) {
			roleTypes = userRoles.stream().map(UserRole::getRoleType).collect(Collectors.toSet());
		}

		return roleTypes;
	}
}
