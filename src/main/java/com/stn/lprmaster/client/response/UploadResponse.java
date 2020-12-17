package com.stn.lprmaster.client.response;

import lombok.Data;

@Data
public class UploadResponse {
    private String message;
    private Data data;

    @lombok.Data
    private static class Data {
        private String ticket_number;
    }
}
