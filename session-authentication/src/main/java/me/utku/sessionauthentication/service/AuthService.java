package me.utku.sessionauthentication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.sessionauthentication.dto.AuthRequest;
import me.utku.sessionauthentication.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public GenericResponse<Boolean> authenticate(AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        GenericResponse<Boolean> authResponse = null;
        try{
            UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(authRequest.username(),authRequest.password());
            Authentication authentication = authenticationManager.authenticate(authToken);
            if (authentication.isAuthenticated()) {
                SecurityContext context = securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authentication);
                securityContextHolderStrategy.setContext(context);
                securityContextRepository.saveContext(context, request, response);
                authResponse = new GenericResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),true);
            }
        }catch (Exception e){
            throw new BadCredentialsException("Failed authentication with USERNAME:"+authRequest.username());
        }
        return authResponse;
    }
}
