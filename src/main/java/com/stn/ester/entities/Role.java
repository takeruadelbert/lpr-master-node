package com.stn.ester.entities;

import com.stn.ester.core.base.OnDeleteRemoveChild;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.services.crud.AccessGroupService;
import com.stn.ester.services.crud.RoleGroupService;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

@Data
@Entity
@OnDeleteRemoveChild.List({
        @OnDeleteRemoveChild(service = AccessGroupService.class, fieldName = "role_id"),
        @OnDeleteRemoveChild(service = RoleGroupService.class, fieldName = "role_id")
})
public class Role extends BaseEntity {

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
