package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.entities.enumerate.RequestMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class ModuleLink extends BaseEntity {

    private static final String COLUMN_MODULE = "module_id";
    private static final String JSON_IGNORE_PROPERTY_MODULE_LINK = "moduleLink";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    private String name;
    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    private String alias;

    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_MODULE)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties(JSON_IGNORE_PROPERTY_MODULE_LINK)
    private Module module;

    @JsonIgnore
    public Module getModule() {
        return module;
    }

    @JsonIgnore
    public void setModule(Module module) {
        this.module = module;
    }

}
