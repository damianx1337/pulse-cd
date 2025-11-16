package com.pulse_project.pulse_cd.domain.models.branch;

import lombok.Value;

import java.time.Instant;

@Value
public class BranchInfo {
    String name;
    String sha;
    Instant lastCommitDate;
}
