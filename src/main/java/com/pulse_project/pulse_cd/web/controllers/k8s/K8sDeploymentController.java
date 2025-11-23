package com.pulse_project.pulse_cd.web.controllers.k8s;


import com.pulse_project.pulse_cd.infrastructure.adapters.k8s.K8sDeploymentService;
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
public class K8sDeploymentController {
    private final K8sDeploymentService k8sDeploymentService;

    public K8sDeploymentController(K8sDeploymentService k8sDeploymentService){
        this.k8sDeploymentService = k8sDeploymentService;
    }

    /**
     * GET /deployments?namespace=default
     * If namespace omitted, returns deployments from all namespaces.
     */
    @GetMapping("/deployments")
    public ResponseEntity<String> getPods(@RequestParam(required = false) String namespace) {
        String podName;
        try {
            podName = k8sDeploymentService.getK8sDeployments();
            return ResponseEntity.ok(podName);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
}