package kr.co.fastcampus.travel.common.exception.handler;

import kr.co.fastcampus.travel.common.exception.BaseException;
import kr.co.fastcampus.travel.common.response.BaseResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<BaseResponseBody> handleBaseException(BaseException e) {
        return new ResponseEntity<>(
            BaseResponseBody.fail(e.getMessage()),
            HttpStatus.OK
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseBody> handleValidException(
        MethodArgumentNotValidException e
    ) {
        return new ResponseEntity<>(
            BaseResponseBody.fail(e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponseBody> handleUnexpectedException(
        Exception e
    ) {
        return new ResponseEntity<>(
            BaseResponseBody.error(e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
