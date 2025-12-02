package com.pulse_project.pulse_cd.domain.models.k8s;

public record K8sPod(Integer id, String name, String namespace, String imageName, String podIp){
    public K8sPod(Integer id, String name, String namespace, String imageName, String podIp) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.imageName = imageName;
        this.podIp = podIp;
    }
}
