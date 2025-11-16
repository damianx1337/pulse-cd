package com.pulse_project.pulse_cd.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/internal")
public class HealthCheckController {
	
	@GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
