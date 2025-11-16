package com.pulse_project.pulse_cd.domain.models.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchResponse {
    private String name;
    private CommitRef commit;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommitRef {
        private String sha;
        private String url;
    }
}
