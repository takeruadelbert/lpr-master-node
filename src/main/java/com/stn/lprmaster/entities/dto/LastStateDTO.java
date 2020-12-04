package com.stn.lprmaster.entities.dto;

import com.stn.lprmaster.entities.DataState;
import com.stn.lprmaster.entities.enumerate.DataStateStatus;
import lombok.Data;

@Data
public class LastStateDTO {
    private Long id;
    private String idGate;
    private String url;
    private DataStateStatus status;
    private LastState lastState;

    public LastStateDTO(DataState dataState, LastState lastState) {
        this.id = dataState.getId();
        this.idGate = dataState.getIdGate();
        this.url = dataState.getUrl();
        this.status = dataState.getStatus();
        this.lastState = lastState;
    }

    @Data
    public static class LastState {
        private String status;
        private Result result;
    }

    @Data
    public static class Result {
        private String vehicleType;
        private String licensePlateNumber;
        private String tokenOutput;
    }
}
