package com.pulse_project.pulse_cd.infrastructure.adapters.dockerhub;

import com.pulse_project.pulse_cd.domain.models.containerimage.DockerTagsPage;
import com.pulse_project.pulse_cd.domain.models.containerimage.TagInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;


@Slf4j
@Service
public class DockerHubService {

    private final WebClient webClient;

    public DockerHubService(WebClient.Builder webClientBuilder,
                            @Value(value = "${dockerhub.url:}") String dockerHubApiBase){
        this.webClient = webClientBuilder
                .baseUrl(dockerHubApiBase)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    /**
     * Returns a Flux<TagInfo> for all tags of namespace/repo by following pagination.
     */
    public Flux<TagInfo> fetchAllTags(String namespace, String repo, int pageSize, String token) {
        String initialUrl = String.format("/%s/%s/tags?page_size=%d", namespace, repo, pageSize);

        // Start by fetching the first page, then expand to follow `next` links while non-null.
        return fetchPage(initialUrl, token)
                .expand(page -> {
                    String next = page.getNext();
                    if (next == null || next.isEmpty()){
                        return Mono.empty();
                    }
                    return fetchPage(next, token);
                })
                .flatMapIterable(DockerTagsPage::getResults)
                .map(tagResult -> new TagInfo(tagResult.getName(), tagResult.getLastUpdated(), tagResult));
    }

    /**
     * Fetch a single page (Mono) and apply basic retry/backoff logic.
     */

    private Mono<DockerTagsPage> fetchPage(String url, String token){
        return webClient
                .get()
                .uri(url)
                .headers(httpHeaders -> {
                    if (token != null && !token.isBlank()) {
                        httpHeaders.setBearerAuth(token);
                    }
                })
                .retrieve()
                .bodyToMono(DockerTagsPage.class)
                // Retry a few times with backoff for transient errors (not for 4xx)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> !(throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode().is4xxClientError()))
                );
    }
}