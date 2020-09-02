package com.stn.ester.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
    private Boolean rememberMe=false;
}
