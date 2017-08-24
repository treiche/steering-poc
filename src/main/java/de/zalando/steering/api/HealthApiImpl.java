package de.zalando.steering.api;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import de.zalando.steering.model.HealthStatus;

@Service("healthApiImpl")
public class HealthApiImpl implements HealthApi {
    @Override
    public ResponseEntity<HealthStatus> healthGet() {
        HealthStatus healthStatus = new HealthStatus();
        healthStatus.setMessage("I'm fine.");
        return ResponseEntity.ok(healthStatus);
    }
}
