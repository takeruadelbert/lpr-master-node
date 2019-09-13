package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

@Data
@Entity
public class UserGroup extends BaseEntity {

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    private String label;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(unique = true)
    private String name;

    @Transient
    private Collection<AccessGroup> accessGroups = new HashSet<AccessGroup>();

    public Collection<AccessGroup> mergeAccessGroup(Collection<AccessGroup> accessGroups) {
        this.accessGroups.addAll(accessGroups);
        return this.accessGroups;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AccessGroup> getAccessGroups() {
        return accessGroups;
    }

}
