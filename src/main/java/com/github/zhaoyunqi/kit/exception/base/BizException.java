package com.github.zhaoyunqi.kit.exception.base;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private int code = 400;

    public BizException(String message) {
        super(message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }


    public static void main(String[] args) {
        System.out.println("hello");
    }

}
