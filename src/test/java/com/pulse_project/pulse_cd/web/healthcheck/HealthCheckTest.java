package com.pulse_project.pulse_cd.web.healthcheck;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles(value = "test")
@SpringBootTest
public class HealthCheckTest {

    private final String testHttpsUrl = "https://example.com/test";
    private final String testUrlWithoutProcotol = "example.com/test";
    private HealthCheck healthCheck;

    @Test
    public void givenHttpsUrl_whenExtractingFqdn_thenReturnFqdn(){
        healthCheck = new HealthCheckImpl();
        Assertions.assertTrue(healthCheck.isReachable(testHttpsUrl));
    }
    @Test
    public void givenUrlWithoutProtocol_whenExtractingFqdn_thenReturnFqdn(){
        healthCheck = new HealthCheckImpl();
        Assertions.assertTrue(healthCheck.isReachable(testUrlWithoutProcotol));
    }
}
