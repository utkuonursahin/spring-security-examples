package me.utku.jwtauthentication.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.GenericResponse;
import me.utku.jwtauthentication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionController extends ResponseEntityExceptionHandler {
    private final AuthService authService;

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericResponse<Boolean>> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        log.info("UsernameNotFoundException: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No user found with this username.",false));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse<Boolean>> badCredentialsExceptionHandler(BadCredentialsException e) {
        log.info("BadCredentialsException: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No match for this username / password.",false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse<Boolean>> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.info("AccessDeniedException: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse<>(HttpStatus.FORBIDDEN.value(), "You don't have rights to access this resource.",false));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<GenericResponse<Boolean>> insufficientAuthenticationExceptionHandler(InsufficientAuthenticationException e) {
        log.info("InsufficientAuthenticationException: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication failed. Be sure you enter your credentials correctly or have rights to access this resource!",false));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<GenericResponse<Boolean>> expiredJwtExceptionHandler(ExpiredJwtException e, HttpServletResponse response) {
        log.info("ExpiredJwtException: {}.", e.getMessage());
        authService.resetJwtCookie(response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Your session has expired.",false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Boolean>> unhandledExceptionHandler(Exception e) {
        log.info("Exception (UNHANDLED): {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.",false));
    }
}
