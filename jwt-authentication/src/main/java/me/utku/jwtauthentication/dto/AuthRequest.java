package me.utku.jwtauthentication.dto;

public record AuthRequest (
        String username,
        String password) { }
