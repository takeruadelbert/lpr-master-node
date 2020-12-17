package com.stn.lprmaster.broker.kafka.model.frame;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameResult {
    @JsonProperty("type")
    private String vehicleType;

    @JsonProperty("license_plate_number")
    private String licensePlateNumber;

    @JsonProperty("token")
    private String tokenOutput;
}
