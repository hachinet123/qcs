package com.tre.centralkitchen.common.exception;

import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.common.enums.ExceptionHandlerEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseResult<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("Request address'{}',Record already exists in the database'{}'", requestURI, e.getMessage());
        return ResponseResult.buildError(ExceptionHandlerEnums.DATA_DUPLICATION_ERROR.getStatus(),
                ExceptionHandlerEnums.DATA_DUPLICATION_ERROR.getCode(), ExceptionHandlerEnums.DATA_DUPLICATION_ERROR.getMsg());
    }

    @ExceptionHandler(BindException.class)
    public ResponseResult<Void> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseResult.buildError(ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getStatus(),
                ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<Void> constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return ResponseResult.buildError(ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getStatus(),
                ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseResult.buildError(ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getStatus(),
                ExceptionHandlerEnums.CUSTOM_VALIDATION_ERROR.getCode(), message);
    }

}
