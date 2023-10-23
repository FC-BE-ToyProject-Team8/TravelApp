package kr.co.fastcampus.travel.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseBody<T> {

    private final Status status;
    private final String errorMessage;
    private final T data;

    public static ResponseBody<Void> ok() {
        return new ResponseBody<>(Status.SUCCESS, null, null);
    }

    public static <T> ResponseBody<T> ok(T data) {
        return new ResponseBody<>(Status.SUCCESS, null, data);
    }

    public static ResponseBody<Void> fail(String errorMessage) {
        return new ResponseBody<>(Status.FAIL, errorMessage, null);
    }

    public static <T> ResponseBody<T> fail(String errorMessage, T data) {
        return new ResponseBody<>(Status.FAIL, errorMessage, data);
    }

    public static ResponseBody<Void> error(String errorMessage) {
        return new ResponseBody<>(Status.ERROR, errorMessage, null);
    }

    public static <T> ResponseBody<T> error(String errorMessage, T data) {
        return new ResponseBody<>(Status.ERROR, errorMessage, data);
    }
}
