package me.utku.basicauthentication.service;

import lombok.RequiredArgsConstructor;
import me.utku.basicauthentication.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.getUserByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
