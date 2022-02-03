package kr.co.parkham.commons.utils;

import java.time.LocalDate;

public class TimeUtils {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static int getThisYear() {
		return LocalDate.now().getYear();
	}

	public static int getThisMonth() {
		return LocalDate.now().getMonthValue();
	}
}

