package me.utku.sessionauthentication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.sessionauthentication.dto.GenericResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body(new GenericResponse<>(status.value(), ex.getMessage(),false));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericResponse<Boolean>> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        log.info("UsernameNotFoundException: {}.", e.getMessage());
        return new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No user found with this username.",false).toResponseEntity();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse<Boolean>> badCredentialsExceptionHandler(BadCredentialsException e) {
        log.info("BadCredentialsException: {}.", e.getMessage());
        return new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No match for this username / password.",false).toResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse<Boolean>> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.info("AccessDeniedException: {}.", e.getMessage());
        return new GenericResponse<>(HttpStatus.FORBIDDEN.value(), "You don't have rights to access this resource.",false).toResponseEntity();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<GenericResponse<Boolean>> insufficientAuthenticationExceptionHandler(InsufficientAuthenticationException e) {
        log.info("InsufficientAuthenticationException: {}.", e.getMessage());
        return new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication failed. Be sure you enter your credentials correctly or have rights to access this resource!",false).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Boolean>> unhandledExceptionHandler(Exception e) {
        log.info("Exception (UNHANDLED): {}.", e.getMessage());
        return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.",false).toResponseEntity();
    }
}
