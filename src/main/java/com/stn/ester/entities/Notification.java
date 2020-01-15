package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Notification extends BaseEntity {
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

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_BOOLEAN_FALSE, nullable = false)
    private boolean seen;

    private String data;
    private String type;

    public Notification() {

    }

    public Notification(Long receiverId, String message, String url, String data, String type) {
        this.receiverId = receiverId;
        this.message = message;
        this.url = url;
        this.data = data;
        this.type = type;
    }

}
