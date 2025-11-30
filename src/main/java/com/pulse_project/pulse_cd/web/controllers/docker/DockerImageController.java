package com.pulse_project.pulse_cd.web.controllers.docker;

import com.pulse_project.pulse_cd.domain.models.containerimage.TagInfo;
import com.pulse_project.pulse_cd.infrastructure.adapters.dockerhub.DockerHubService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/docker")
public class DockerImageController {
    private final DockerHubService dockerHubService;

    public DockerImageController (DockerHubService dockerHubService) {
        this.dockerHubService = dockerHubService;
    }

    /**
     * Streams tag info for the given namespace/repo.
     *
     * Example:
     * GET /api/docker/library/ubuntu/tags?page_size=100
     *
     * Optional auth token (for private repos or higher rate limits):
     * GET /api/docker/library/ubuntu/tags?token=eyJ...
     */
    @GetMapping(value = "/{namespace}/{repo}/tags", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TagInfo> streamTags (@PathVariable String namespace,
                                     @PathVariable String repo,
                                     @RequestParam(name = "page_size", defaultValue = "100") int pageSize,
                                     @RequestParam(name = "token", required = false) String token
    ) {
        return dockerHubService.fetchAllTags(namespace, repo, pageSize, token);
    }
}
