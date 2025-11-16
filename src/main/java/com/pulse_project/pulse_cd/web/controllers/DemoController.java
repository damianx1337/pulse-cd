package com.pulse_project.pulse_cd.web.controllers;

import com.pulse_project.pulse_cd.infrastructure.adapters.k8s.K8sPodService;
import com.pulse_project.pulse_cd.web.healthcheck.HealthCheck;
import com.pulse_project.pulse_cd.web.healthcheck.HealthCheckImpl;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    private HealthCheck healthCheck;
    private K8sPodService k8sPodService;
	
	@GetMapping("/ping")
    public String ping() {

        healthCheck = new HealthCheckImpl();
        healthCheck.isReachable("example.com");

        try {
            k8sPodService.getK8sPods();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return "pong";
    }
}