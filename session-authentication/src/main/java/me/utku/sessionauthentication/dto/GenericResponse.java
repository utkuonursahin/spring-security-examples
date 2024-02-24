package me.utku.sessionauthentication.dto;

public record GenericResponse<T> (int statusCode, String message, T data){ }
