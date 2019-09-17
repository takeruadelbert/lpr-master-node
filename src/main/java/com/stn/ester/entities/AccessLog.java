package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stn.ester.entities.enumerate.RequestMethod;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

public class AccessLog implements Serializable {
    private String IPAddress;
    private String URI;

    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;
    private String requestBody;
    private Long userId;

    @CreatedDate
    @CreationTimestamp
    @JsonIgnore
    private Date createdDate;

    public AccessLog() {

    }

    public AccessLog(String IPAddress, String URI, RequestMethod requestMethod, Long userId, String requestBody) {
        this.IPAddress = IPAddress;
        this.URI = URI;
        this.requestMethod = requestMethod;
        this.userId = userId;
        this.requestBody = requestBody;
    }
}
