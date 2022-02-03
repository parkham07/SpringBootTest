package kr.co.parkham.config.handler;

import kr.co.parkham.commons.Common;
import kr.co.parkham.commons.ErrorCode;
import kr.co.parkham.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorResponseAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> errorResponse(BindException ex) {
        log.error(Common.getPrintStackTrace(ex));

        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        String[] errorCodeName = null;
        String codeName  = null;
        String getMsg = null;

        if(defaultMessage != null) {
            String[] errorCodeName = defaultMessage.split(":",2);
            codeName = errorCodeName[0];
            getMsg = errorCodeName[1];
        }

        ErrorCode errorCode = ErrorCode.parse(codeName);
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code(errorCode.getCode())
                                                    .msg(errorCode.getMsg(getMsg))
                                                    .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> errorResponse(Exception ex) {
        log.error(Common.getPrintStackTrace(ex));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
