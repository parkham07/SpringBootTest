package kr.co.parkham.config.encoder;

import kr.co.parkham.commons.Common;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

public class CustomPasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		if(rawPassword == null || rawPassword.length() <= 0) {
			return null;
		}

		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch(Exception e) {
			Common.getPrintStackTrace(e);

			return null;
		}

		md.update(rawPassword.toString().getBytes());

		return new String(Base64.encodeBase64(md.digest()));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		var password = encode(rawPassword);

		if(password == null) {
			return false;
		}

		return password.equals(encodedPassword);
	}
}
