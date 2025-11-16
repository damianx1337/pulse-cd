package com.pulse_project.pulse_cd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class GithubClientConfiguration {
    @Value(value = "${github.api-base:https://api.github.com}")
    private String githubApiBase;

    @Value("${github.token:}")
    private String githubToken;

    @Bean
    public WebClient githubWebClient(){
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(githubApiBase)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json");

        if (githubToken != null && !githubToken.isBlank()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken);
        }
        // optional: simple log filter for requests/responses during development
        builder.filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // remove token from logs if needed; this is minimal
            return Mono.just(clientRequest);
        }));

        return builder.build();
    }
}
