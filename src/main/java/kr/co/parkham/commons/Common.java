package kr.co.parkham.commons;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Common {
	private Common() {
		throw new IllegalStateException("Only Utility class");
	}

	public static String getClientIp(HttpServletRequest request) {
		final String text = "unknown";
		String result = request.getHeader("X-Forwarded-For");

		if(result == null || result.length() <= 0 || text.equalsIgnoreCase(result)) {
			result = request.getHeader("Proxy-Client-IP");
		}

		if(result == null || result.length() <= 0 || text.equalsIgnoreCase(result)) {
			result = request.getHeader("WL-Proxy-Client-IP");
		}

		if(result == null || result.length() <= 0 || text.equalsIgnoreCase(result)) {
			result = request.getHeader("HTTP_CLIENT_IP");
		}

		if(result == null || result.length() <= 0 || text.equalsIgnoreCase(result)) {
			result = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if(result == null || result.length() <= 0 || text.equalsIgnoreCase(result)) {
			result = request.getRemoteAddr();
		}

		return result;
	}

	public static String getPrintStackTrace(Exception e) {
		StringWriter errors = new StringWriter();

		e.printStackTrace(new PrintWriter(errors));

		return errors.toString();
	}
}
