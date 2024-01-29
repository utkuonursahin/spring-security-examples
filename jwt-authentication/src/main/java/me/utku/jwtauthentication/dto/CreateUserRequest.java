package me.utku.jwtauthentication.dto;

import lombok.Builder;
import me.utku.jwtauthentication.enums.Role;

import java.util.Set;

@Builder
public record CreateUserRequest(String name,
                                String username,
                                String password,
                                Set<Role> authorities
) { }
