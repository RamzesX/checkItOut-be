package com.sm.instagram.instagramPlatform.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
class GlobalDefaultExceptionHandler extends DefaultHandlerExceptionResolver {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong, please send us info about error";

    @ExceptionHandler(Exception.class)
    public void defaultErrorHandler(Exception e, HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) throw e;

        logger.error(e.getMessage(), e);
        setOrder(Ordered.LOWEST_PRECEDENCE);

        doResolveException(req, res, handler, e);
        if(isErrorUnHandled(res)) res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR_MESSAGE);
    }

    private boolean isErrorUnHandled(HttpServletResponse res) {
        return res.getStatus() == HttpStatus.OK.value();
    }
}