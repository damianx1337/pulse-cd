package com.pulse_project.pulse_cd.web.healthcheck;


/**
 * Common interface for HealthCheck
 */

public interface HealthCheck {
    public boolean isReachable(String host);
}
