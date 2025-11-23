package com.pulse_project.pulse_cd.web.controllers.k8s;


import com.pulse_project.pulse_cd.ApiClient;
import com.pulse_project.pulse_cd.domain.models.k8s.K8sPod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = "test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class K8SPodControllerTest {
	
	private static ClientAndServer mockServer;
    private static int mockServerPort = 1080;
    private static MockServerClient mockServerClient;
    private static ApiClient apiClient;

    private static String data = "{\"username\": \"foo\", \"password\": \"bar\"}";
		
	@BeforeAll
	public static void startMockServer() {
		mockServer = ClientAndServer.startClientAndServer(mockServerPort);
        mockServerClient = new MockServerClient("127.0.0.1", mockServerPort);
        String baseUrl = "http://127.0.0.1:" + mockServerPort;

        apiClient = new ApiClient(baseUrl);

        setupExpectations();
	}
	
	@AfterAll
	public static void stopMockServer() {
        if (mockServer != null){
            mockServer.stop();
        }
	}

	private static void setupExpectations() {
        mockServerClient.when(
                request()
                        .withMethod("GET")
                        .withPath("/api/users"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400"))
                                .withBody("""
                                        [
                                            { "id": 1, "name": "Alice", "email": "alice@example.com" },
                                            { "id": 2, "name": "Bob",   "email": "bob@example.com" }
                                        ]
                                        """)
                );
	}

    @Test
    void listUsers_shouldReturnTwoUsers(){
        List<K8sPod> k8sPods = apiClient.listUsers().collectList().block();
        assertThat(k8sPods).isNotNull();
        assertThat(k8sPods).hasSize(2);
        assertThat(k8sPods.get(0).name()).isEqualTo("Alice");
        assertThat(k8sPods.get(1).name()).isEqualTo("Bob");
    }
	
}
