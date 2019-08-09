package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccessLog extends AppDomain {
    public static final String unique_name = "access_log";

    @Column(name = "ip_address", nullable = false)
    private String IPAddress;

    @Column(nullable = false)
    private String URI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestMethod requestMethod;

    @Column(name = "request_body", length = 9999)
    private String requestBody;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    @JsonProperty("userId")
    private Long userId;

    @JsonSetter("userId")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upload_file_id", insertable = false, updatable = false)
    private AssetFile uploadFile;

    @JsonProperty("uploadFileId")
    @Column(name = "upload_file_id")
    private Long uploadFileId;

    public void setUploadFileId(Long uploadFileId) {
        if (uploadFileId != null)
            this.uploadFileId = uploadFileId;
    }

    public AccessLog() {

    }

    public AccessLog(String IPAddress, String URI, RequestMethod requestMethod, Long userId, String requestBody) {
        this.IPAddress = IPAddress;
        this.URI = URI;
        this.requestMethod = requestMethod;
        this.userId = userId;
        this.requestBody = requestBody;
    }

    public String underscoreName() {
        return unique_name;
    }
}
