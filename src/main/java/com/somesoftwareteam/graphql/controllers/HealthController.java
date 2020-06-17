package com.somesoftwareteam.graphql.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping( "/health" )
    public String register() {
        return "Success";
    }
}
