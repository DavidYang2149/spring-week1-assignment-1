package com.codesoom.assignment.models;

/**
 * HTTP 상태를 정의합니다.
 * https://datatracker.ietf.org/doc/html/rfc7231#section-6
 */
public enum HttpStatus {
    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
