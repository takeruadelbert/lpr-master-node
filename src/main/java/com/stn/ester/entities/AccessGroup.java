package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class AccessGroup extends BaseEntity {

    private static final String COLUMN_USER_GROUP = "user_group_id";
    private static final String COLUMN_MENU = "menu_id";
    private static final String JSON_PROPERTY_USER_GROUP = "userGroupId";
    private static final String JSON_PROPERTY_MENU = "menuId";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_USER_GROUP, insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private UserGroup userGroup;

    @NotNull(message = EntityConstant.MESSAGE_NOT_BLANK)
    @JsonProperty(JSON_PROPERTY_USER_GROUP)
    @Column(name = COLUMN_USER_GROUP)
    private long userGroupId;

    @JsonSetter(JSON_PROPERTY_USER_GROUP)
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_MENU, insertable = false, updatable = false)
    private Menu menu;

    @NotNull(message = EntityConstant.MESSAGE_NOT_BLANK)
    @JsonProperty(JSON_PROPERTY_MENU)
    @Column(name = COLUMN_MENU)
    private long menuId;

    @JsonSetter(JSON_PROPERTY_MENU)
    public void setMenuId(long menuId) {
        if (menuId != 0)
            this.menuId = menuId;
    }

    private boolean viewable;
    private boolean editable;
    private boolean addable;
    private boolean deleteable;

    public AccessGroup() {

    }

    public AccessGroup(long userGroupId, long menuId, boolean viewable, boolean editable, boolean addable, boolean deleteable) {
        this.userGroupId = userGroupId;
        this.menuId = menuId;
        this.viewable = viewable;
        this.editable = editable;
        this.addable = addable;
        this.deleteable = deleteable;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public boolean isViewable() {
        return viewable;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isAddable() {
        return addable;
    }

    public boolean isDeleteable() {
        return deleteable;
    }

    public long getMenuId() {
        return menuId;
    }

    public Menu getMenu() {
        return menu;
    }
}
