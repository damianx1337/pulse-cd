package com.pulse_project.pulse_cd.config;


import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class K8sClientConfig {
    /**
     * Shared global ApiClient bean.
     * You can optionally configure timeouts, SSL, custom kubeconfig, etc.
     */

    @Bean
    public ApiClient apiClient() throws IOException {
        ApiClient apiClient = ClientBuilder.defaultClient();
        return apiClient;
    }

    /**
     * Shared AppsV1Api bean (Deployment, StatefulSet, DaemonSet, ReplicaSet...)
     */
    @Bean
    public AppsV1Api appsV1Api(ApiClient apiClient) {
        return new AppsV1Api(apiClient);
    }

    /**
     * Shared CoreV1Api bean (Pods, Services, ConfigMaps, Secrets...)
     */
    @Bean
    public CoreV1Api coreV1Api(ApiClient apiClient){
        return new CoreV1Api(apiClient);
    }
}