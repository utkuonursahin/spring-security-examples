package me.utku.sessionauthentication.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.sessionauthentication.dto.AuthRequest;
import me.utku.sessionauthentication.dto.CreateUserRequest;
import me.utku.sessionauthentication.dto.GenericResponse;
import me.utku.sessionauthentication.model.User;
import me.utku.sessionauthentication.service.AuthService;
import me.utku.sessionauthentication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public User signup(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<Boolean>> login(@RequestBody AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        GenericResponse<Boolean> authResponse = authService.authenticate(authRequest, request ,response);
        return ResponseEntity.status(authResponse.statusCode()).body(authResponse);
    }
}
