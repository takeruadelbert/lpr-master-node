package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.constant.EntityConstant;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Notification extends AppDomain {
    public static final String unique_name = "notification";
    private static final String COLUMN_RECEIVER = "receiver_id";
    private static final String JSON_PROPERTY_RECEIVER = "receiverId";

    private String message;
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_RECEIVER, insertable = false, updatable = false)
    private User receiver;

    @Column(name = COLUMN_RECEIVER)
    @JsonProperty(JSON_PROPERTY_RECEIVER)
    private Long receiverId;

    @JsonSetter(JSON_PROPERTY_RECEIVER)
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Column(columnDefinition = EntityConstant.COLUMN_DEFAULT_INIT_ZERO, nullable = false)
    private int hasSeen;

    public Notification() {

    }

    public Notification(Long receiverId, String message, String url) {
        this.receiverId = receiverId;
        this.message = message;
        this.url = url;
    }

    public void setToHasSeen() {
        this.hasSeen = 1;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
