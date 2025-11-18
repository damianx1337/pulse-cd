package com.pulse_project.pulse_cd.web.controllers;

import com.pulse_project.pulse_cd.web.healthcheck.HealthCheck;
import com.pulse_project.pulse_cd.web.healthcheck.HealthCheckImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/internal")
public class HealthCheckController {

    private HealthCheck healthCheck;

	@GetMapping("/ping")
    public String ping() {

        healthCheck = new HealthCheckImpl();
        healthCheck.isReachable("example.com");

        return "pong";
    }
}
