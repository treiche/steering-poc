package de.zalando.steering.nakadi.Event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterdataEvent extends AbstractDataChangeEvent<MasterDataDefinition> {

    public MasterdataEvent(@JsonProperty("metadata") final Metadata metadata,
            @JsonProperty("data_type") final String dataType,
            @JsonProperty("data_op") final DataOperation dataOp,
            @JsonProperty("data") final MasterDataDefinition data) {
        super(metadata, dataType, dataOp, data);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("MasterdataEvent{");

        Metadata metadata = this.getMetadata();
        if (metadata != null) {
            sb.append("eid='").append(metadata.getEid()).append('\'');
            sb.append(", flowId='").append(metadata.getFlowId()).append('\'');
            sb.append(", occurredAt='").append(metadata.getOccurredAt()).append('\'');
        }

        sb.append(", dataType='").append(this.getDataType()).append('\'');
        sb.append(", dataOp='").append(this.getDataOp()).append('\'');
        sb.append(", data=").append(this.getData());
        sb.append('}');
        return sb.toString();
    }
}
