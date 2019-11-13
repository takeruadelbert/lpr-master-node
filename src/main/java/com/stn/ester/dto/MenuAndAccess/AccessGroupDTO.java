package com.stn.ester.dto.MenuAndAccess;

import com.stn.ester.entities.AccessGroup;
import lombok.Data;

@Data
public class AccessGroupDTO {

    private boolean viewable;
    private boolean editable;
    private boolean addable;
    private boolean deleteable;
    private Long menuId;
    private Long id;

    public AccessGroupDTO(AccessGroup accessGroup) {
        this.viewable = accessGroup.isViewable();
        this.editable = accessGroup.isEditable();
        this.addable = accessGroup.isAddable();
        this.deleteable = accessGroup.isDeleteable();
        this.id = accessGroup.getId();
        this.menuId = accessGroup.getMenuId();
    }

}
