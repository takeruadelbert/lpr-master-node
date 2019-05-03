package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccessGroup extends AppDomain {

    public static final String unique_name="access_group";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    private UserGroup userGroup;

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

    @Override
    public String underscoreName() {
        return AccessGroup.unique_name;
    }
}
