package com.Abhishek.OauthResourseserver.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/api/users")
    public String[] getUser(){
        return new String[]{"Abhishek","Ankit","Shivam"};
    }
}
