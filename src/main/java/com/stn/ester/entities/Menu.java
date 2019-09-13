package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.etc.base.OnDeleteSetParentNull;
import com.stn.ester.etc.base.TableFieldPair;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.services.crud.MenuService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@OnDeleteSetParentNull({
        @TableFieldPair(service = MenuService.class, fieldName = "parent_menu_id")
})
public class Menu extends BaseEntity {

    private static final String COLUMN_MODULE = "module_id";
    private static final String COLUMN_PARENT_MENU = "parent_menu_id";
    private static final String JSON_PROPERTY_MODULE = "moduleId";
    private static final String JSON_PROPERTY_PARENT_MENU = "parentMenuId";
    private static final String JSON_IGNORE_PROPERTIES_FIELD = "subMenu";

    private String label;
    private int orderingNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_MODULE, insertable = false, updatable = false)
    private Module module;

    @JsonProperty(JSON_PROPERTY_MODULE)
    @Column(name = COLUMN_MODULE)
    private Long moduleId;

    public Menu() {

    }

    @JsonSetter(JSON_PROPERTY_MODULE)
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Menu> subMenu = new HashSet<Menu>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_PARENT_MENU, insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties(JSON_IGNORE_PROPERTIES_FIELD)
    private Menu parentMenu;

    @JsonProperty(JSON_PROPERTY_PARENT_MENU)
    @Column(name = COLUMN_PARENT_MENU)
    private Long parentMenuId;

    @JsonSetter(JSON_PROPERTY_PARENT_MENU)
    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public String getLabel() {
        return label;
    }

    public void mergeSubMenu(List<Menu> subMenu) {
        this.subMenu.addAll(subMenu);
    }

    public Module getModule() {
        return module;
    }

}
