package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class IdentityType extends BaseEntity {

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    public String label;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false)
    private String name;

}
