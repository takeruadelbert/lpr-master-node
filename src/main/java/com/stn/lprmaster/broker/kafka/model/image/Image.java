package com.stn.lprmaster.broker.kafka.model.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.lprmaster.broker.kafka.model.frame.FrameResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @JsonProperty("ticket_number")
    private String ticketNumber;
    private FrameResult result;
}
