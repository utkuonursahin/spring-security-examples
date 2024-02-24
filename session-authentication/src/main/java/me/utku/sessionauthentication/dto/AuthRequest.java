package me.utku.sessionauthentication.dto;

public record AuthRequest (
        String username,
        String password) { }
