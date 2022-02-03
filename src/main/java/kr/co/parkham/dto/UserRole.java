package kr.co.parkham.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserRole implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 4697273156773762174L;

	private RoleType roleType;

	public enum RoleType {
		ROLE_ADMIN, ROLE_VIEW, ROLE_GUEST;

		public static RoleType parse(String r) {
			if(r == null) {
				return null;
			}

			for(RoleType roleType: RoleType.values()) {
				if(roleType.ordinal() == Integer.parseInt(r)) {
					return roleType;
				}
			}

			return null;
		}
	}

	public UserRole(RoleType roleType) {
		this.roleType = roleType;
	}

	@Override
	public String getAuthority() {
		return this.roleType.name();
	}
}
