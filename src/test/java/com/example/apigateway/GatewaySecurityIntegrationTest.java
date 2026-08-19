package com.example.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GatewaySecurityIntegrationTest {
    @Autowired private WebTestClient client;

    @Test
    void rejectsProtectedRoutesWithoutJwt() {
        client.get().uri("/api/notifications/unread")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void exposesHealthWithoutJwt() {
        client.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }
}
