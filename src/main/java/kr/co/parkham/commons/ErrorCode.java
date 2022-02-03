package kr.co.parkham.commons;

import lombok.Getter;

@Getter
public enum ErrorCode {
	STR_LENGTH("1000", "[#VALUE1]글자의 값을 입력해주세요.")
	, STR_MIN_LENGTH("1001", "최소 [#VALUE1]글자의 값을 입력해주세요.")
	, STR_MAX_LENGTH("1002", "최대 [#VALUE1]글자의 값을 입력해주세요.")
	, STR_MIN_MAX_LENGTH("1003", "최소 [#VALUE1]글자, 최대 [#VALUE2]글자의 값을 입력해주세요.")

	, UNKNOWN("9999", "잘못된 요청입니다.")
	;

	private String code;
	private String msg;

	ErrorCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ErrorCode parse(String name) {
		if(name == null) {
			return UNKNOWN;
		}

		for(ErrorCode e:ErrorCode.values()) {
			if(e.name().equals(name)) {
				return e;
			}
		}

		return UNKNOWN;
	}

	public String getMsg(String... data) {
		String result = msg;

		for(int i = 1; i <= data.length; i++) {
			result = result.replace("[#VALUE" + i + "]", data[i-1]);
		}

		return result;
	}
}
