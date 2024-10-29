package com.envelope.exception;

import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleObjectAlreadyExistsException(ObjectAlreadyExistsException e) {
        return new ErrorResponse(
                "ObjectNotFound",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        return resolveMethodArgumentNotValidException(e);
    }

    // Default error message is too big and tricky to read
    private ErrorResponse resolveMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return new ErrorResponse(
                "NotValid",
                bindingResult.getFieldError().getDefaultMessage()
        );
    }

}
