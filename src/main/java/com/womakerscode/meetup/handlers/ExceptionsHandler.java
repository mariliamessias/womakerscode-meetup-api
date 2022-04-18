package com.womakerscode.meetup.handlers;

import com.womakerscode.meetup.exceptions.GenericErrorsException;
import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.NotAllowedException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericErrorsException handleValidateException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return new GenericErrorsException(bindingResult);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericErrorsException handleResourceNotFoundException(ResourceNotFoundException e) {
        return new GenericErrorsException(e);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericErrorsException handleBusinessException(BusinessException e) {
        return new GenericErrorsException(e);
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public GenericErrorsException handleNotAllowedException(NotAllowedException e) {
        return new GenericErrorsException(e);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus
    public ResponseEntity handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity(new GenericErrorsException(ex), ex.getStatus());
    }
}
