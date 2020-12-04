package com.stn.lprmaster.entities.dto;

import lombok.Data;

@Data
public class LastState {
    private String status;
    private Result result;

    @Data
    private class Result {
        private String vehicleType;
        private String licensePlateNumber;
        private String tokenOutput;
    }
}
