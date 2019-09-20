package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.ester.entities.enumerate.RequestMethod;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class AccessLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("IpAddress")
    private String IPAddress;

    @JsonProperty("URI")
    private String URI;

    @Enumerated(EnumType.STRING)
    @JsonProperty("requestMethod")
    private RequestMethod requestMethod;

    @JsonProperty("requestBody")
    private String requestBody;

    @CreatedDate
    @CreationTimestamp
    @JsonIgnore
    private Date createdDate;

    @JsonProperty("userId")
    private Long userId;

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
