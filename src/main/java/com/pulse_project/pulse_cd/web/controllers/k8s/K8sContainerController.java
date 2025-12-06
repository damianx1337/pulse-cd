package com.pulse_project.pulse_cd.web.controllers.k8s;

import com.pulse_project.pulse_cd.infrastructure.adapters.k8s.K8sContainerService;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/k8s/api/container")
public class K8sContainerController {
    @Autowired
    K8sContainerService k8sContainerService;

    @PostMapping("/modifyEnvs")
    public ResponseEntity<String> modifyContainerEnvVars() throws ApiException {

        Map<String, String> vars = Map.of(
                "MY_KEY", "123",
                "NEW_FEATURE_ENABLED", "eeeeeeee"
        );

        k8sContainerService.addEnvVars(
                "kube-system",
                "coredns",
                "coredns",
                vars
        );

        return ResponseEntity.ok("Ok");
    }
}
