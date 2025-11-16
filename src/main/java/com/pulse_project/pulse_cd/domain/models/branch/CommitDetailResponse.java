package com.pulse_project.pulse_cd.domain.models.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDetailResponse {
    private InnerCommit commit;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InnerCommit {
        private Author author;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Author {
            private String name;
            private String email;
            // ISO 8601 date, e.g. "2025-11-01T12:34:56Z"
            private String date;
        }
    }
}
