package kr.co.parkham.vo;

import kr.co.parkham.commons.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	@Builder.Default
	private String result = ErrorCode.UNKNOWN.getCode();
	@Builder.Default
	private String code = ErrorCode.UNKNOWN.getCode();
	@Builder.Default
	private String msg = ErrorCode.UNKNOWN.getMsg();
}