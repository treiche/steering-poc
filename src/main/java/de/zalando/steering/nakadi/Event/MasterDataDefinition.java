package de.zalando.steering.nakadi.Event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterDataDefinition {

    private final String code;
    private final MasterDataType masterDataType;

    public MasterDataDefinition(@JsonProperty("master_data_type") final MasterDataType masterDataType,
            @JsonProperty("code") final String code) {
        this.masterDataType = masterDataType;
        this.code = code;
    }

    public MasterDataType getMasterDataType() {
        return masterDataType;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MasterDataDefinition{");
        sb.append("code='").append(code).append('\'');
        sb.append(", masterDataType=").append(masterDataType);
        sb.append('}');
        return sb.toString();
    }
}
