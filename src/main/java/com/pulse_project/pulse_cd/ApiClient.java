package com.pulse_project.pulse_cd;

import com.pulse_project.pulse_cd.domain.models.k8s.K8sPod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ApiClient {
    private final WebClient webClient;

    public ApiClient(String baseUrl){
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        //BlockHound.install();
    }

    // List users
    public Flux<K8sPod> listUsers() {
        return webClient.get()
                .uri("/api/users")
                .retrieve()
                .bodyToFlux(K8sPod.class);
    }

    // Get single user
    public Mono<K8sPod> getUser(int id) {
        return webClient.post()
                .uri("/api/users/{id}", id)
                .retrieve()
                .bodyToMono(K8sPod.class);
    }

    // Example for error status handling (returns raw status code as int)
    public Mono<Integer> callStatusEndpoint(String uri) {
        return webClient.get()
                .uri(uri)
                .exchangeToMono(resp -> Mono.just(resp.statusCode().value()));
    }
}
