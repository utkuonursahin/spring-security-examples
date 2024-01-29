package me.utku.basicauthentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping
    public String helloWorld(){
        return "Hello World! from Private Controller";
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String helloWorldUser(){
        return "Hello World! from Private Controller for User";
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String helloWorldAdmin(){
        return "Hello World! from Private Controller for Admin";
    }
}
