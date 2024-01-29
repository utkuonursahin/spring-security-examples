package me.utku.inmemoryauth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1 = User.builder()
                .username("tony")
                .password(bCryptPasswordEncoder().encode("1234"))
                .authorities("ROLE_USER")
                .build();

        UserDetails admin = User.builder()
                .username("ania")
                .password(bCryptPasswordEncoder().encode("1234"))
                .authorities("ROLE_ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1,admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                /*formLogin(Customizer.withDefaults()) will generate a login page
                if disabled credentials should be sent with http header*/
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("/public/**","/auth/**").permitAll())
                //a role is a granted authority with ROLE_ prefix
                .authorizeHttpRequests(req -> req.requestMatchers("/private/user/**").hasRole("USER")) //will add ROLE_ prefix
                .authorizeHttpRequests(req -> req.requestMatchers("/private/admin/**").hasRole("ADMIN")) //will add ROLE_ prefix
                .authorizeHttpRequests(req -> req.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
