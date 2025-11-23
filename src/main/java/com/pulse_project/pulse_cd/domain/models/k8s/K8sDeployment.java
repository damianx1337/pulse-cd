package com.pulse_project.pulse_cd.domain.models.k8s;

public record K8sDeployment(Integer id, String name, String namespace, String imageName){
    public K8sDeployment(Integer id, String name, String namespace, String imageName) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.imageName = imageName;
    }
}