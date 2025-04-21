package com.amusing.start.exception;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/20
 */
@Getter
public class InnerApiException extends RuntimeException {

    private static final long serialVersionUID = 5270836599966149836L;

    private final String code;

    private final String message;

    public InnerApiException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
