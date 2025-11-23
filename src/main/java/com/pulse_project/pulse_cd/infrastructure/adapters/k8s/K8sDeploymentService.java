package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class K8sDeploymentService {
    private final AppsV1Api appsV1Api;

    public K8sDeploymentService(AppsV1Api appsV1Api) {
        this.appsV1Api = appsV1Api;
    }


    public String getK8sDeployments() throws ApiException {
        //V1DeploymentList deploymentList = appsV1Api.listDeploymentForAllNamespaces().execute();
        V1DeploymentList deploymentList = appsV1Api.listNamespacedDeployment("kube-system").execute();
        if (!deploymentList.getItems().isEmpty()) {
            for (V1Deployment item : deploymentList.getItems()) {
                log.info(item.getSpec().getTemplate().getSpec().getContainers().getFirst().getImage());
                item.getSpec().getTemplate().getSpec().getContainers().getFirst().setImage("registry.k8s.io/coredns/coredns:v1.12.1");
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
