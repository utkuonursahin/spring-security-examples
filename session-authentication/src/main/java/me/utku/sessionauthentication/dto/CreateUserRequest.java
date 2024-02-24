package me.utku.sessionauthentication.dto;

import lombok.Builder;
import me.utku.sessionauthentication.enums.Role;

import java.util.Set;

@Builder
public record CreateUserRequest(String name,
                                String username,
                                String password,
                                Set<Role> authorities
) { }
