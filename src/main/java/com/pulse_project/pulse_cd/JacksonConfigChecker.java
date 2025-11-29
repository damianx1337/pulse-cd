package com.pulse_project.pulse_cd;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfigChecker implements CommandLineRunner {
    private final ObjectMapper mapper;
    public JacksonConfigChecker(ObjectMapper mapper) { this.mapper = mapper; }

    @Override
    public void run(String... args) {
        JsonInclude.Value v = mapper.getSerializationConfig().getDefaultPropertyInclusion();
        System.out.println("Jackson default-property-inclusion: " + v);
    }
}