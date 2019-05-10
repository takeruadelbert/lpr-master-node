package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Menu extends AppDomain {

    public static final String unique_name="menu";

    private String label;
    private int orderingNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", insertable = false, updatable = false)
    private Module module;

    @JsonProperty("moduleId")
    @Column(name = "module_id")
    private Long moduleId;

    @JsonSetter("moduleId")
    public void setModuleId(long moduleId) {
        if (moduleId != 0)
            this.moduleId = moduleId;
    }

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Menu> subMenu=new HashSet<Menu>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_menu_id", insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Menu parentMenu;

    @JsonProperty("parentMenuId")
    @Column(name="parent_menu_id")
    private Long parentMenuId;

    @JsonSetter("parentMenuId")
    public void setParentMenuId(long parentMenuId){
        if (parentMenuId!=0)
            this.parentMenuId=parentMenuId;
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public void mergeSubMenu(List<Menu> subMenu){
        this.subMenu.addAll(subMenu);
    }

    @Override
    public String underscoreName() {
        return Menu.unique_name;
    }
}
