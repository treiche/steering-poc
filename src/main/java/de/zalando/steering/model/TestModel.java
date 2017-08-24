package de.zalando.steering.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestModel {

    @JsonProperty
    private String code;

    @JsonProperty
    private String type;

    @JsonProperty
    private String eventType;

    @JsonProperty
    private String operation;

    @JsonProperty
    private DateTime occurredAt;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public DateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(final DateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

}
