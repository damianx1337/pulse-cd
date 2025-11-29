/*package com.pulse_project.pulse_cd.infrastructure.adapters.dockerhub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class DockerHubService {
    @Value(value = "${dockerhub.url:}")
    private String dockerHubApiBase;

    private final WebClient webClient;

    public DockerHubService(WebClient webClient, String dockerHubApiBase){
        this.webClient = WebClient.builder()
                .baseUrl(dockerHubApiBase)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }
}
*/