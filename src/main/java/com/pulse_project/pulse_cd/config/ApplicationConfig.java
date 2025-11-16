package com.pulse_project.pulse_cd.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Bean
    public RetryRegistry retryRegistry(){
        // default config applied to all names unless overridden
        RetryConfig defaultRetryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .failAfterMaxAttempts(true)
                .retryExceptions(IOException.class)
                .ignoreExceptions(IllegalArgumentException.class)
                .build();

        RetryRegistry registry = RetryRegistry.of(defaultRetryConfig);

        // register a named override (for example, a flaky third-party endpoint)
        RetryConfig specialBackendConfig = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(250))
                .retryExceptions(java.io.IOException.class, java.net.SocketTimeoutException.class)
                .build();

        registry.addConfiguration("specialBackend*", specialBackendConfig);
        // now any retry name starting with "specialBackend" will use the override

        // Example: subscribe to events for monitoring/logging for the default instance
        Retry defaultRetry = registry.retry("default");
        defaultRetry.getEventPublisher()
                .onRetry(event -> log.info("Retry on {} attempt: {}, last exception: {}",
                        event.getName(),
                        event.getNumberOfRetryAttempts(),
                        event.getLastThrowable() == null ? "none" : event.getLastThrowable().toString()))
                .onSuccess(event -> log.info("Successful after retry: {}", event.getNumberOfRetryAttempts()))
                .onError(event -> log.warn("Retries exhausted for {} after {} attempts",
                        event.getName(), event.getNumberOfRetryAttempts()));

        return registry;
    }

    // convenience bean: create a named Retry and expose as bean (optional)
    @Bean
    public Retry backendServiceRetry(RetryRegistry retryRegistry){
        // obtains retry with name "backendService"; uses default config unless pattern matched
        Retry retry = retryRegistry.retry("backendService");
        // you can attach per-instance subscribers too
        retry.getEventPublisher().onRetry(event -> log.debug("[backendService] retry attempt {} cause {}", event.getNumberOfRetryAttempts(), event.getLastThrowable()));
        return retry;
    }
}
