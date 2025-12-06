package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class K8sContainerService {
    private final AppsV1Api appsV1Api;


    public K8sContainerService(AppsV1Api appsV1Api) {
        this.appsV1Api = appsV1Api;
    }

    public void addEnvVars(String namespace, String deploymentName, String containerName, Map<String, String> envVars) throws ApiException {

        V1Deployment deployment = appsV1Api.readNamespacedDeployment(deploymentName,namespace).execute();

        V1Container container = deployment.getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .stream()
                .filter(c -> c.getName().equals(containerName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Container '" + containerName + "' not found in deployment " + deploymentName
                ));

        List<V1EnvVar> updatedEnv = new ArrayList<>();
        if (container.getEnv() != null)
            updatedEnv.addAll(container.getEnv());

        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            updatedEnv.removeIf(e -> e.getName().equals(entry.getKey())); // overwrite
            updatedEnv.add(new V1EnvVar().name(entry.getKey()).value(entry.getValue()));
        }

        V1Deployment patch = new V1Deployment()
                .spec(new V1DeploymentSpec()
                        .template(new V1PodTemplateSpec()
                                .spec(new V1PodSpec()
                                        .containers(List.of(
                                                new V1Container()
                                                        .name(containerName)
                                                        .env(updatedEnv)
                                        ))
                                )
                        )
                );

        log.info("CONTAINER NAME: {}", container.getName());
        log.info("CONTAINER IMG: {}", container.getImage());
        assert container.getEnv() != null;
        log.info("CONTAINER ENV VARS: {}", container.getEnv().size());
        log.info("DEPLOYMENT NAME: {}", deployment.getMetadata().getName());
        log.info("DEPLOYMENT ENV VARS: {}", deployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getEnv().size());
        log.info("DEPLOYMENT PATCH ENV VARS: {}", patch.getSpec().getTemplate().getSpec().getContainers().getFirst().getEnv().size());
//        deployment.setSpec(patch.getSpec());
        deployment.getSpec().getTemplate().getSpec().getContainers().getFirst().setEnv(patch.getSpec().getTemplate().getSpec().getContainers().getFirst().getEnv());

        log.info("DEPLOYMENT ENV VARS (AFTER): {}", deployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getEnv().size());
        log.info("DEPLOYMENT PATCH ENV VARS (AFTER): {}", patch.getSpec().getTemplate().getSpec().getContainers().getFirst().getEnv().size());

        appsV1Api.replaceNamespacedDeployment(
                deploymentName,
                "kube-system",
                deployment
        ).execute();
    }
}
