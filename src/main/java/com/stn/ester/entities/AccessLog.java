package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.enumerate.RequestMethod;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccessLog extends BaseEntity {
    private static final String COLUMN_IP_ADDRESS = "ip_address";
    private static final String COLUMN_REQUEST_BODY = "request_body";
    private static final String COLUMN_USER = "user_id";
    private static final String COLUMN_UPLOAD_FILE = "upload_file_id";
    private static final String JSON_PROPERTY_USER = "userId";
    private static final String JSON_PROPERTY_UPLOAD_FILE = "uploadFileId";

    @Column(name = COLUMN_IP_ADDRESS, nullable = false)
    private String IPAddress;

    @Column(nullable = false)
    private String URI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestMethod requestMethod;

    @Column(name = COLUMN_REQUEST_BODY, length = 9999)
    private String requestBody;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_USER, nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = COLUMN_USER)
    @JsonProperty(JSON_PROPERTY_USER)
    private Long userId;

    @JsonSetter(JSON_PROPERTY_USER)
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_UPLOAD_FILE, insertable = false, updatable = false)
    private AssetFile uploadFile;

    @JsonProperty(JSON_PROPERTY_UPLOAD_FILE)
    @Column(name = COLUMN_UPLOAD_FILE)
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
}
