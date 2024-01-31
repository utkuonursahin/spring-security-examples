package me.utku.jwtauthentication.dto;

import org.springframework.http.HttpStatus;

public record AuthResponse(Boolean isAuthenticated, String message, HttpStatus httpStatus) { }
