package kr.co.fastcampus.travel.exception.handler;

import com.example.convention.common.exception.BaseException;
import com.example.convention.common.response.BaseResponseBody;
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
            BaseResponseBody.error(e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseBody> handleValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
            BaseResponseBody.error(e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }
}
