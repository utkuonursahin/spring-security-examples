package me.utku.jwtauthentication.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.GenericResponse;
import me.utku.jwtauthentication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GenericResponse<Boolean>> usernameNotFoundException(UsernameNotFoundException e) {
        log.warn("Username not found: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No match for this username / password.",false));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse<Boolean>> badCredentialsException(UsernameNotFoundException e) {
        log.warn("Bad credentials: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "No match for this username / password.",false));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<GenericResponse<Boolean>> accessDeniedException(InsufficientAuthenticationException e) {
        log.warn("Access denied: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse<>(HttpStatus.FORBIDDEN.value(), "You don't have rights to access this resource.",false));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<GenericResponse<Boolean>> expiredJwtException(ExpiredJwtException e, HttpServletResponse response) {
        log.warn("Expired JWT: {}.", e.getMessage());
        authService.resetJwtCookie(response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Your session has expired.",false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Boolean>> unhandledExceptions(Exception e) {
        log.warn("Unhandled exception occurred: {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.",false));
    }
}
