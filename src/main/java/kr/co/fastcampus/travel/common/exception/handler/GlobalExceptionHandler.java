package kr.co.fastcampus.travel.common.exception.handler;

import kr.co.fastcampus.travel.common.exception.BaseException;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BaseException.class)
    public ResponseBody<Void> handleBaseException(BaseException e) {
        log.warn("[BaseException] Message = {}", e.getMessage());
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseBody<Void> handleValidException(MethodArgumentNotValidException e) {
        log.warn("[MethodArgumentNotValidException] Message = {}",
            NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            String message = "[Request Error] "
                + fieldError.getField()
                + "="
                + fieldError.getRejectedValue()
                + " ("
                + fieldError.getDefaultMessage()
                + ")";
            return ResponseBody.fail(message);
        }
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseBody<Void> handleUnexpectedException(Exception e) {
        log.error("[Exception]", e);
        return ResponseBody.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseBody<Void> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] Message = {}", e.getMessage());
        return ResponseBody.fail(e.getMessage());
    }
}
