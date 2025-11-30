package com.pulse_project.pulse_cd.domain.models.containerimage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.Value;

@Value
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagResult {

    String name;

    @JsonProperty("last_updated")
    String lastUpdated;

    public TagResult(String name, String lastUpdated){
        this.name = name;
        this.lastUpdated = lastUpdated;
    }


}
