package com.pulse_project.pulse_cd.web.controllers;

import com.pulse_project.pulse_cd.domain.models.branch.BranchInfo;
import com.pulse_project.pulse_cd.infrastructure.adapters.github.GitHubService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/repos")
public class BranchesController {
    private final GitHubService gitHubService;

    @Value("${github.default-active-days:30}")
    private int defaultActiveDays;

    public BranchesController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    /**
     * GET /api/repos/{owner}/{repo}/branches?activeWithinDays=30
     *
     * If activeWithinDays is omitted, uses application property github.default-active-days
     */
    @GetMapping("/{owner}/{repo}/branches")
    public Mono<ResponseEntity<List<BranchInfo>>> getActiveBranches(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(required = false) Integer activeWithinDays) {

        int days = (activeWithinDays == null) ? defaultActiveDays : activeWithinDays;
        return gitHubService.getActiveBranches(owner, repo, days)
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).build()));
    }
}
