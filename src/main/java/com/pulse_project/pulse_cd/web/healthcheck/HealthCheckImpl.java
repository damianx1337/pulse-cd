package com.pulse_project.pulse_cd.web.healthcheck;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class HealthCheckImpl implements HealthCheck {

    private String extractFqdn (String inputUrl){
        try {
            // Ensure URL has a protocol for URL parsing
            if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://")) {
                inputUrl = "https://" + inputUrl;
            }

            URL url = new URL(inputUrl);
            return url.getHost(); // returns the FQDN
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + inputUrl);
        }
    }

    @Override
    public boolean isReachable(String host) {
        return true;
    }
}
