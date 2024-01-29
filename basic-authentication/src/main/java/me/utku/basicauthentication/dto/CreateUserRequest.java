package me.utku.basicauthentication.dto;

import lombok.Builder;
import me.utku.basicauthentication.enums.Role;

import java.util.Set;

@Builder
public record CreateUserRequest (String name,
                                 String username,
                                 String password,
                                 Set<Role> authorities
) { }
