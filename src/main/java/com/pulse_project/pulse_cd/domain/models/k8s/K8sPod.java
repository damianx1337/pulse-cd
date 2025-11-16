package com.pulse_project.pulse_cd.domain.models.k8s;


public record K8sPod(Integer id, String name, String email, String imageName){
    public K8sPod(Integer id, String name, String email, String imageName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageName = imageName;
    }
}
