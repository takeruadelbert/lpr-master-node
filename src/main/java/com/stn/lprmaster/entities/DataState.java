package com.stn.lprmaster.entities;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.lprmaster.entities.enumerate.DataStateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DataState extends BaseEntity {

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(unique = true)
    private String url;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(unique = true)
    private String idGate;
    private String lastState;

    @Enumerated(EnumType.STRING)
    private DataStateStatus status = DataStateStatus.RUNNING;
}
