package com.neckfurry.finalexam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to FinalExam Application!";
    }

    @GetMapping("/api/public/health")
    public String health() {
        return "Application is running!";
    }
}