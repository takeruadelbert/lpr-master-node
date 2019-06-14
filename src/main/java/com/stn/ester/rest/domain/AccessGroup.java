package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
public class AccessGroup extends AppDomain {

    public static final String unique_name="access_group";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private UserGroup userGroup;

    @NotNull(message = "User Group is mandatory.")
    @JsonProperty("userGroupId")
    @Column(name = "user_group_id")
    private long userGroupId;

    @JsonSetter("userGroupId")
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

    @NotNull(message = "Menu is mandatory.")
    @JsonProperty("menuId")
    @Column(name = "menu_id")
    private long menuId;

    @JsonSetter("menuId")
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

    public long getMenuId() {
        return menuId;
    }

    @Override
    public String underscoreName() {
        return AccessGroup.unique_name;
    }
}
