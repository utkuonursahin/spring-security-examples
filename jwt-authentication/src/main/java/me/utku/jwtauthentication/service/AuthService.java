package me.utku.jwtauthentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String authenticateAndSendToken(AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(request.username());
        }
        log.info("invalid username " + request.username());
        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }
}
