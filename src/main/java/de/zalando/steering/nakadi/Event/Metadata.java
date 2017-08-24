package de.zalando.steering.nakadi.Event;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Metadata {
    private final String eventType;
    private final String eid;
    private final DateTime occurredAt;
    private final DateTime receivedAt;
    private final String flowId;

    @JsonCreator
    @Deprecated
    private Metadata(@JsonProperty("event_type") final String eventType,
            @JsonProperty("eid") final String eid,
            @JsonProperty("occurred_at") final String occurredAt,
            @JsonProperty("received_at") final String receivedAt,
            @JsonProperty("flow_id") final String flowId) {
        this(eventType, eid, occurredAt == null ? null : DateTime.parse(occurredAt),
            receivedAt == null ? null : DateTime.parse(receivedAt), flowId);
    }

    public Metadata(final String eventType, final String eid, final DateTime occurredAt, final DateTime receivedAt,
            final String flowId) {
        this.eventType = eventType;
        this.eid = eid;
        this.occurredAt = occurredAt;
        this.receivedAt = receivedAt;
        this.flowId = flowId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEid() {
        return eid;
    }

    public DateTime getOccurredAt() {
        return occurredAt;
    }

    public DateTime getReceivedAt() {
        return receivedAt;
    }

    public String getFlowId() {
        return flowId;
    }
}
