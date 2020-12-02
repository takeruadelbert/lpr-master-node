package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class EsterLicense extends BaseEntity {


    private LocalDate expire;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean checkExpire;

    private String licenseTo;
}
