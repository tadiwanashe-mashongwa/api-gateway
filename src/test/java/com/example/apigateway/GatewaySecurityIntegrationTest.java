package com.example.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "cors.allowed-origin=https://sparelink-frontend.vercel.app"
)
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

    @Test
    void acceptsFrontendCorsPreflightForPublicCatalogueRequests() {
        client.options().uri("/api/parts?status=ACTIVE&size=20")
                .header(HttpHeaders.ORIGIN, "https://sparelink-frontend.vercel.app")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "https://sparelink-frontend.vercel.app"
                );
    }
}
