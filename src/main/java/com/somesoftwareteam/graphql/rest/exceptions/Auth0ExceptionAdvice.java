package com.somesoftwareteam.graphql.rest.exceptions;

import com.auth0.exception.Auth0Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Auth0ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(Auth0Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String fixtureNotFoundHandler(Auth0Exception ex) {
        return ex.getMessage();
    }
}
