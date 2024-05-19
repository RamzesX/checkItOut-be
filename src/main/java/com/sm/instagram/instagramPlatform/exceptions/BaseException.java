package com.sm.instagram.instagramPlatform.exceptions;

import jdk.jfr.StackTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseException extends RuntimeException {
    BaseException(String message, Throwable cause) {
        super(message, cause);
        log.info(this.getMessage(), this);
    }
}
