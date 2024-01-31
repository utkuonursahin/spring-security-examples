package me.utku.jwtauthentication.dto;

public record GenericResponse<T> (int status, String message, T data){ }
