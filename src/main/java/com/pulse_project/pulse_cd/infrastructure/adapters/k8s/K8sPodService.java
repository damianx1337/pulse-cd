package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
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
public class K8sPodService {

    private ApiClient apiClient;
    private CoreV1Api coreV1Api;
    private AppsV1Api appsV1Api;

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
                if (checkPodMetadataEmpty(item)){
                    setContainerImageInPod(item);
                    return item.getMetadata().getName();
                }
            }
        }
        return "POD WITH NO NAME";
    }

    private boolean checkPodMetadataEmpty(V1Pod k8sPod) {
        if (k8sPod.getMetadata() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkContainerListEmpty(V1Pod k8sPod) {
        if (k8sPod.getSpec() != null) {
            return !k8sPod.getSpec().getContainers().isEmpty();
        } else {
            return false;
        }
    }

    private String setContainerImageInPod(V1Pod k8sPod) {
        if (checkContainerListEmpty(k8sPod)) {
//            int containerCount = countContainers(k8sPod);
//            List<V1Container> containers = Collections.singletonList(k8sPod.getSpec().getContainers().get(0));
            log.info("BEFORE SET: {}", k8sPod.getSpec().getContainers().getFirst().getImage());
            k8sPod.getSpec().getContainers().getFirst().setImage("registry.k8s.io/coredns/coredns:v1.12.0");
            log.info("AFTER SET: {}",k8sPod.getSpec().getContainers().getFirst().getImage());
            return k8sPod.getSpec().getContainers().getFirst().getName();
        }
        return "NO CONTAINER NAME FOUND!";
    }

    private String setContainerImageInDeployment(V1Deployment k8sDeployment) {
        k8sDeployment.getSpec().getTemplate().getSpec().getContainers().getFirst().setImage("registry.k8s.io/coredns/coredns:v1.12.0");
        return "No Deployment found!";
    }

    private int countContainersInPod (V1Pod k8sPod) {
        return k8sPod.getSpec().getContainers().size();
    }
}
