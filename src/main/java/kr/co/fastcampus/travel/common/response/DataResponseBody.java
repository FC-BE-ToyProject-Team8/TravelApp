package kr.co.fastcampus.travel.common.response;

import lombok.Getter;

@Getter
public class DataResponseBody<T> extends BaseResponseBody {
    private final T data;

    public DataResponseBody(Status status, String errorMessage, T data) {
        super(status, errorMessage);
        this.data = data;
    }

    public static <T> DataResponseBody<T> ok(T data) {
        return new DataResponseBody<>(Status.SUCCESS, null, data);
    }

    public static <T> DataResponseBody<T> fail(String errorMessage, T data) {
        return new DataResponseBody<>(Status.FAIL, errorMessage, data);
    }

    public static <T> DataResponseBody<T> error(String errorMessage, T data) {
        return new DataResponseBody<>(Status.ERROR, errorMessage, data);
    }
}
