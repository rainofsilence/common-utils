package com.lindsey.coutil.sql.res;

public class BuildingResult<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    private String code;
    private String message;
    private T data;

    public static <E> BuildingResult<E> success() {
        return new BuildingResult<E>(SUCCESS, null, null);
    }

    public static <E> BuildingResult<E> failed(String message) {
        return new BuildingResult<E>(FAILED, message, null);
    }

    public BuildingResult() {
    }

    public BuildingResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "BuildingResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
