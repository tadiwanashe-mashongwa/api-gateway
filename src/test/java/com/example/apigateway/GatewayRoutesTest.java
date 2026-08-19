package com.example.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewayRoutesTest {
    private final RouteLocator routes;
    @Autowired
    GatewayRoutesTest(RouteLocator routes) { this.routes = routes; }

    @Test
    void exposesOneRouteForEverySpareLinkBackendService() {
        StepVerifier.create(routes.getRoutes().map(route -> route.getId()).collectList())
                .assertNext(ids -> assertThat(ids).containsExactlyInAnyOrderElementsOf(Set.of(
                        "catalogue-service", "inventory-service", "order-service", "payment-service",
                        "customer-service", "shipping-service", "notification-service")))
                .verifyComplete();
    }
}
