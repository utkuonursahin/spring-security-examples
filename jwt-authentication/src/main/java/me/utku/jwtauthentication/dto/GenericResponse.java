package me.utku.jwtauthentication.dto;

public record GenericResponse<T> (int statusCode, String message, T data){ }
