package com.pulse_project.pulse_cd.web.controllers;

import com.pulse_project.pulse_cd.infrastructure.adapters.k8s.K8sService;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/k8s/api")
public class K8sController {
    private final K8sService k8sService;

    public K8sController(K8sService k8sService){
        this.k8sService = k8sService;
    }

    /**
     * GET /pods?namespace=default
     * If namespace omitted, returns pods from all namespaces.
     */
	@GetMapping("/pods")
    public ResponseEntity<String> getPods(@RequestParam(required = false) String namespace) {
        String podName;
        try {
            podName = k8sService.getK8sPods();
            return ResponseEntity.ok(podName);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
}