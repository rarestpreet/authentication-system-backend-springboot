package com.project.authentication_system.exception;

public class TokenExpiredException extends Throwable {
    public TokenExpiredException(String message) {
        super(message);
    }
}
