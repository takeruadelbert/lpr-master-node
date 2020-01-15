package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class NotificationRule extends BaseEntity {

    private String type;
    private String target;
    private String action;
}
