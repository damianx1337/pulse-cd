package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Service
public class K8sDeploymentService {
    private ApiClient apiClient;
    private AppsV1Api appsV1Api;


    /**
     * Initialize the ApiClient & AppsV1Api once on application startup.
     * ClientBuilder.defaultClient() will pick up in-cluster config when running inside k8s,
     * or the user's kubeconfig otherwise.
     */
    @PostConstruct
    public void init() throws IOException {
        // Build the client (auto-detects in-cluster vs kubeconfig)
        this.apiClient = ClientBuilder.defaultClient();
        Configuration.setDefaultApiClient(apiClient);
        this.appsV1Api = new AppsV1Api();
    }
    @PreDestroy
    public void shutdown() {
        if (apiClient != null && apiClient.getHttpClient() != null) {
            try {
                // shut down executor and evict pooled connections
                apiClient.getHttpClient().dispatcher().executorService().shutdown();
                apiClient.getHttpClient().connectionPool().evictAll();
            } catch (Exception e) {
                log.error("Failed to clean up ApiClient HTTP resources: {}", e.getMessage());
            }
        }
    }

    public String getK8sDeployments() throws ApiException {
        //V1DeploymentList deploymentList = appsV1Api.listDeploymentForAllNamespaces().execute();
        V1DeploymentList deploymentList = appsV1Api.listNamespacedDeployment("kube-system").execute();
        if (!deploymentList.getItems().isEmpty()) {
            for (V1Deployment item : deploymentList.getItems()) {
                log.info(item.getSpec().getTemplate().getSpec().getContainers().getFirst().getImage());
                item.getSpec().getTemplate().getSpec().getContainers().getFirst().setImage("registry.k8s.io/coredns/coredns:v1.11.0");
                log.info(item.getSpec().getTemplate().getSpec().getContainers().getFirst().getImage());
                setK8sDeploymentImage(item);
                return item.getMetadata().getName();
            }
        }
        return "Deployment with no name";
    }

    public void setK8sDeploymentImage(V1Deployment k8sDeployment) throws ApiException {
        // Push update
        V1Deployment updated = appsV1Api.replaceNamespacedDeployment(
                k8sDeployment.getMetadata().getName(),
                "kube-system",
                k8sDeployment
        ).execute();
    }
}
