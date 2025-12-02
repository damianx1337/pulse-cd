package com.pulse_project.pulse_cd.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

//@ActiveProfiles(value = "test")
@Configuration
public class MockK8sClientConfig {
    @Bean
    @Primary
    public ApiClient apiClient(){
        return mock(ApiClient.class);
    }
    @Bean
    @Primary
    public AppsV1Api appsV1Api(){
        return mock(AppsV1Api.class);
    }
    @Bean
    @Primary
    public CoreV1Api coreV1Api(){
        return mock(CoreV1Api.class);
    }
}
