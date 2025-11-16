package com.pulse_project.pulse_cd.infrastructure.adapters.github;

import com.pulse_project.pulse_cd.domain.models.branch.BranchInfo;
import com.pulse_project.pulse_cd.domain.models.branch.BranchResponse;
import com.pulse_project.pulse_cd.domain.models.branch.CommitDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GitHubService {
    private final WebClient webClient;

    public GitHubService (WebClient webClient){
        this.webClient = webClient;
    }

    /**
     * Fetches branches and returns those with last commit within the given days threshold.
     *
     * @param owner owner/org
     * @param repo  repository name
     * @param activeWithinDays number of days to consider a branch "active"
     */
    public Mono<List<BranchInfo>> getActiveBranches(String owner, String repo, int activeWithinDays){
        String branchesPath = String.format("/repos/%s/%s/branches", owner, repo);

        // Fetch branches (set per_page=100 to minimize pagination; for >100 you'll need to follow Link headers)
        Flux<BranchResponse> branchesFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(branchesPath)
                        .queryParam("per_page", "100")
                        .build())
                .retrieve()
                .bodyToFlux(BranchResponse.class)
                .timeout(Duration.ofSeconds(20));

        Instant threshold = Instant.now().minus(activeWithinDays, ChronoUnit.DAYS);

        // For every branch, fetch the commit details for commit.sha and map to BranchInfo.
        return branchesFlux.flatMap(branch -> {
                    String sha = branch.getCommit().getSha();
                    String commitPath = String.format("/repos/%s/%s/commits/%s", owner, repo, sha);
                    Mono<CommitDetailResponse> commitMono = webClient.get()
                            .uri(commitPath)
                            .retrieve()
                            .bodyToMono(CommitDetailResponse.class)
                            .timeout(Duration.ofSeconds(10))
                            .onErrorResume(e -> Mono.empty()); // skip branches we couldn't fetch the commit for

                    return commitMono.map(cd -> {
                        String dateStr = cd.getCommit().getAuthor().getDate();
                        Instant commitInstant = Instant.parse(dateStr);
                        return new BranchInfo(branch.getName(), sha, commitInstant);
                    });
                })
                .filter(bi -> bi.getLastCommitDate().isAfter(threshold))
                .collectList();
    }
}
