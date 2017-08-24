package de.zalando.steering.api;

import org.springframework.http.ResponseEntity;

import de.zalando.steering.model.HealthStatus;

public interface HealthApi {

    ResponseEntity<HealthStatus> healthGet();
}
