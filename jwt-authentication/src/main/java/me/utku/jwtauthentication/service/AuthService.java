package me.utku.jwtauthentication.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.jwtauthentication.dto.AuthRequest;
import me.utku.jwtauthentication.dto.GenericResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //To use jwt authentication with cookies, you should uncomment the following code block.
    /*private ResponseCookie createJwtCookie(String value, int maxAge, String path) {
        return ResponseCookie.from("jwt", value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(maxAge)
                .path(path)
                .build();
    }

    public void resetJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
*/
    public GenericResponse<Boolean> authenticateAndSendToken (AuthRequest request, HttpServletResponse httpServletResponse) {
        GenericResponse<Boolean> authResponse = null;
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(request.username());
                //To use jwt authentication with cookies, you can use the following commented code block instead of the authorization header.
                /*ResponseCookie cookie = createJwtCookie(jwt, 3600, "/");
                httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());*/
                httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION, jwt);
                authResponse = new GenericResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),true);
            }
        }catch (Exception e){
            throw new BadCredentialsException("Failed authentication with USERNAME:"+request.username()+" / PASSWORD:"+request.password()+")");
        }
        return authResponse;
    }
}