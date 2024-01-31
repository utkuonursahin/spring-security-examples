package me.utku.jwtauthentication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.AuthRequest;
import me.utku.jwtauthentication.dto.AuthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private ResponseCookie createCookie(String name, String value, int maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .maxAge(maxAge)
                .path(path)
                .build();
    }

    public Optional<AuthResponse> authenticateAndSendToken (AuthRequest request, HttpServletResponse httpServletResponse) {
        Optional<AuthResponse> authResponse = Optional.empty();
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(request.username());
                ResponseCookie cookie = createCookie("jwt", jwt, 3600, "/");
                httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                authResponse = Optional.of(new AuthResponse(true, "Authentication successful", HttpStatus.OK));
            }
        }catch (Exception e){
            log.error("Error while authenticating user: {}", e.getMessage());
            authResponse = Optional.of(new AuthResponse(false, "Authentication failed because of "+e.getMessage().toLowerCase(), HttpStatus.UNAUTHORIZED));
        }
        return authResponse;
    }
}
