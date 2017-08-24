package de.zalando.steering;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class SteeringProperties {

    private String applicationName;
    private String consumerGroup;
    private String eventName;
    private Integer nakadiBatchLimit;
    private Integer nakadiStreamTimeout;
    private Integer nakadiMaxUncommittedEvents;
    private Integer fluxConcurrency;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(final String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

    public Integer getNakadiBatchLimit() {
        return nakadiBatchLimit;
    }

    public void setNakadiBatchLimit(final Integer nakadiBatchLimit) {
        this.nakadiBatchLimit = nakadiBatchLimit;
    }

    public Integer getNakadiStreamTimeout() {
        return nakadiStreamTimeout;
    }

    public void setNakadiStreamTimeout(final Integer nakadiStreamTimeout) {
        this.nakadiStreamTimeout = nakadiStreamTimeout;
    }

    public Integer getNakadiMaxUncommittedEvents() {
        return nakadiMaxUncommittedEvents;
    }

    public void setNakadiMaxUncommittedEvents(final Integer nakadiMaxUncommittedEvents) {
        this.nakadiMaxUncommittedEvents = nakadiMaxUncommittedEvents;
    }

    public Integer getFluxConcurrency() {
        return fluxConcurrency;
    }

    public void setFluxConcurrency(final Integer fluxConcurrency) {
        this.fluxConcurrency = fluxConcurrency;
    }
}
