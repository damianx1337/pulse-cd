package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Service
public class K8sService {

    private ApiClient apiClient;
    private CoreV1Api coreV1Api;

    /**
     * Initialize the ApiClient & CoreV1Api once on application startup.
     * ClientBuilder.defaultClient() will pick up in-cluster config when running inside k8s,
     * or the user's kubeconfig otherwise.
     */

    @PostConstruct
    public void init() throws IOException {
        // Build the client (auto-detects in-cluster vs kubeconfig)
        this.apiClient = ClientBuilder.defaultClient();
        Configuration.setDefaultApiClient(apiClient);
        this.coreV1Api = new CoreV1Api();
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

    /**
     * Public method to list pods. If namespace is null or blank, lists pods across all namespaces.
     *
     * @param namespace namespace to list pods from, or null/blank to list across all namespaces
     * @return list of PodDto
     * @throws ApiException when Kubernetes API call fails
     */
    public String getK8sPods() throws ApiException {
        V1PodList podList = coreV1Api.listPodForAllNamespaces().execute();
        if (!podList.getItems().isEmpty()) {
            for (V1Pod item : podList.getItems()) {
                assert item.getMetadata() != null;
                return item.getMetadata().getName();
            }
        }
        return "POD WITH NO NAME";
    }
}
