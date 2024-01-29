package me.utku.basicauthentication;

import lombok.RequiredArgsConstructor;
import me.utku.basicauthentication.dto.CreateUserRequest;
import me.utku.basicauthentication.enums.Role;
import me.utku.basicauthentication.model.User;
import me.utku.basicauthentication.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class BasicAuthenticationApplication implements CommandLineRunner {
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createDummyData();
    }

    private void createDummyData(){

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("tony")
                .username("tony")
                .password("pass")
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        userService.create(createUserRequest);

        CreateUserRequest createUserRequest2 = CreateUserRequest.builder()
                .name("ania")
                .username("ania")
                .password("pass")
                .authorities(Set.of(Role.ROLE_ADMIN))
                .build();

        userService.create(createUserRequest2);
    }
}
