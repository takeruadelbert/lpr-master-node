package com.stn.ester.dto;

import com.stn.ester.entities.AccessGroup;
import lombok.Data;

import java.util.Collection;

@Data
public class PrivilegeDTO {

    private boolean viewable;
    private boolean addable;
    private boolean editable;
    private boolean deleteable;

    public PrivilegeDTO(AccessGroup accessGroup) {
        this.viewable = accessGroup.isViewable();
        this.addable = accessGroup.isAddable();
        this.editable = accessGroup.isEditable();
        this.deleteable = accessGroup.isDeleteable();
    }

    public PrivilegeDTO(Collection<AccessGroup> accessGroups) {
        for (AccessGroup accessGroup : accessGroups) {
            this.viewable |= accessGroup.isViewable();
            this.addable |= accessGroup.isAddable();
            this.editable |= accessGroup.isEditable();
            this.deleteable |= accessGroup.isDeleteable();
        }
    }

}
