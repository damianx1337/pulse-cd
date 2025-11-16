package com.pulse_project.pulse_cd;

import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    private final RetryRegistry retryRegistry;

    public ExternalApiService(RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
    }

    /**
     * Example method demonstrating how to get a Retry by name and use it
     * to decorate a supplier that calls a remote endpoint.
     */
}
