package com.card.controller;

import com.card.controller.objs.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(e.getMessage()));
    }
}
