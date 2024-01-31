package me.utku.jwtauthentication.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.AuthRequest;
import me.utku.jwtauthentication.dto.CreateUserRequest;
import me.utku.jwtauthentication.dto.GenericResponse;
import me.utku.jwtauthentication.model.User;
import me.utku.jwtauthentication.service.AuthService;
import me.utku.jwtauthentication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public User addUser(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<Boolean>> generateToken(@RequestBody AuthRequest authRequest, HttpServletResponse httpServletResponse) {
        GenericResponse<Boolean> authResponse = authService.authenticateAndSendToken(authRequest, httpServletResponse);
        return ResponseEntity.status(authResponse.status()).body(authResponse);
    }
}
