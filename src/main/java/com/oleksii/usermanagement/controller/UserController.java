package com.oleksii.usermanagement.controller;

import com.oleksii.usermanagement.controller.response.ExceptionResponse;
import com.oleksii.usermanagement.domain.UserCreateResource;
import com.oleksii.usermanagement.domain.UserEntity;
import com.oleksii.usermanagement.exception.UserNotFoundException;
import com.oleksii.usermanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;

@RestController()
@RequestMapping("/service/user/realm")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity createUser(@RequestBody @Valid UserCreateResource userCreateResource) {
        return userService.createUser(userCreateResource);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Handling exceptions

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse invalidId() {
        return new ExceptionResponse("InvalidArgument");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse userNotFound(UserNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse nameIsNul() {
        return new ExceptionResponse("InvalidRealmName");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse duplicateName() {
        return new ExceptionResponse("DuplicateRealmName");
    }
}
