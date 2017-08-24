package de.zalando.steering.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.zalando.steering.model.HealthStatus;

@RestController
@RequestMapping(value = "/api")
public final class HealthApiController implements HealthApi {

    private HealthApi healthApi;

    @Autowired
    public HealthApiController(@Qualifier("healthApiImpl") final HealthApi healthApi) {
        this.healthApi = healthApi;
    }

    @Override
    @RequestMapping(
        value = "/health", method = RequestMethod.GET, produces = {
            "application/x.zalando.steer+json", "application/problem+json"
        }
    )
    public ResponseEntity<HealthStatus> healthGet() {
        return this.healthApi.healthGet();
    }

}
