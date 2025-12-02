package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import com.pulse_project.pulse_cd.config.MockK8sClientConfig;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.reset;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MockK8sClientConfig.class, K8sDeploymentService.class })
public class K8sDeploymentServiceTest {
    @Autowired
    private AppsV1Api appsV1Api;

    @Autowired
    private K8sDeploymentService deploymentService;

    @BeforeEach
    public void setup(){
        reset(appsV1Api);
    }
/*
    private V1Deployment makeDeployment(String name, String namespace, List<String> images){
        V1ObjectMeta meta = new V1ObjectMeta().name(name).namespace(namespace).labels(Map.of("app", name));
        var containers = images.stream()
                .map(img -> new V1Container().name(name + "-ctr").image(img))
                .toList();
    }

 */
}
