package com.pulse_project.pulse_cd.web.controllers.k8s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pulse_project.pulse_cd.infrastructure.adapters.k8s.K8sConfigMapService;
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
@RequestMapping("/k8s/api/configmap")
public class K8sConfigMapController {
    @Autowired
    K8sConfigMapService k8sConfigMapService;

    @PostMapping("/patch")
    public ResponseEntity<String> modifyConfigMap() throws IOException, ApiException {
        //var result = k8sConfigMapService.patchConfigMap(
        k8sConfigMapService.patchConfigMap(
                "my-config",
                "default",
                Map.of("NEW_KEY", "new_value_22222",
                        "EXISTING_KEY", "updated_value"
                )
        );
        return ResponseEntity.ok("Ok");
    }
}
