package com.pulse_project.pulse_cd.domain.models.containerimage;


import lombok.AccessLevel;
import lombok.Setter;
import lombok.Value;

/**
 * Returned to clients. Contains the tag name, lastUpdated, and raw payload (TagResult).
 */

@Value
@Setter(AccessLevel.NONE)
public class TagInfo {
    String name;
    String lastUpdated;
    Object raw;
}
