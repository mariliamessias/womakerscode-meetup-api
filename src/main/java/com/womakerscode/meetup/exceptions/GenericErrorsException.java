package com.womakerscode.meetup.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericErrorsException {

    private final List<String> errors;

    public GenericErrorsException(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public GenericErrorsException(BusinessException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public GenericErrorsException(NotAllowedException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public GenericErrorsException(ResourceNotFoundException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public GenericErrorsException(ResponseStatusException e) {
        this.errors = Arrays.asList(e.getReason());
    }

    public List<String> getErrors() {
        return errors;
    }
}
