package com.pulse_project.pulse_cd.domain.models.containerimage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Value
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerTagsPage {
    String next;
    List<TagResult> results;

    public DockerTagsPage(String next, List<TagResult> results){
        this.next = next;
        this.results = results;
    }


}
