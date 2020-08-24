package com.github.zhaoyunqi.kit.msg;

public class RestResponse<T> extends BaseResponse {

    T data;

    public RestResponse success(T data, String message) {
        this.data = data;
        this.setMessage(message);
        return this;
    }

    public RestResponse success(T data) {
        this.data = data;
        this.setMessage("");
        return this;
    }

    public RestResponse result(int status, String message) {
        this.setMessage(message);
        this.setStatus(status);
        return this;
    }

    public RestResponse result(int status, T data, String message) {
        this.setStatus(status);
        this.setData(data);
        this.setMessage(message);
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
