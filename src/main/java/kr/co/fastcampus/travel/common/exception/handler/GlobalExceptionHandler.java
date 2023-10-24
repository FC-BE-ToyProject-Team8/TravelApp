package kr.co.fastcampus.travel.common.exception.handler;

import kr.co.fastcampus.travel.common.exception.BaseException;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BaseException.class)
    public ResponseBody<Void> handleBaseException(BaseException e) {
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseBody<Void> handleValidException(MethodArgumentNotValidException e) {
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseBody<Void> handleUnexpectedException(Exception e) {
        return ResponseBody.error(e.getMessage());
    }
}
