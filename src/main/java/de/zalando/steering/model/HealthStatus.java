package de.zalando.steering.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthStatus {

    private String message = null;

    /**
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class HealthStatus {");

        sb.append("  message: ").append(message).append("");
        sb.append("}");
        return sb.toString();
    }
}
