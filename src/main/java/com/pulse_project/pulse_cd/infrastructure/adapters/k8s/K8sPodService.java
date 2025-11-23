package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class K8sPodService {

    private final CoreV1Api coreV1Api;

    public K8sPodService(CoreV1Api coreV1Api){
        this.coreV1Api = coreV1Api;
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
