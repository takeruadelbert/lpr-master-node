package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Notification extends AppDomain {
    public static final String unique_name = "notification";

    private String message;
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;

    @Column(name = "receiver_id")
    @JsonProperty("receiverId")
    private Long receiverId;

    @JsonSetter("receiverId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Column(columnDefinition = "tinyint default 0")
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
