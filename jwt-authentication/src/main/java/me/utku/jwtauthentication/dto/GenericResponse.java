package me.utku.jwtauthentication.dto;

public record GenericResponse<T> (T data, int httpStatusCode, String message){ }
