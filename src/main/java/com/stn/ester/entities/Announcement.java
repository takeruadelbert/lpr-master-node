package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.ester.entities.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Announcement extends BaseEntity {

    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    @JsonProperty("userId")
    private Long userId;
}
