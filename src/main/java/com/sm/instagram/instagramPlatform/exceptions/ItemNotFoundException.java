package com.sm.instagram.instagramPlatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends BaseException {
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
