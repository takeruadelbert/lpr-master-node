package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import com.stn.ester.rest.base.OnDeleteSetParentNull;
import com.stn.ester.rest.base.TableFieldPair;
import com.stn.ester.rest.service.MenuService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@OnDeleteSetParentNull({
        @TableFieldPair(service = MenuService.class, tableName = "menu", fieldName = "parent_menu_id")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Menu extends AppDomain {

    public static final String unique_name = "menu";

    private long id;
    private String label;
    private int orderingNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", insertable = false, updatable = false)
    private Module module;

    @JsonProperty("moduleId")
    @Column(name = "module_id")
    private Long moduleId;

    public Menu() {

    }

    @JsonSetter("moduleId")
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Menu> subMenu = new HashSet<Menu>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_menu_id", insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Menu parentMenu;

    @JsonProperty("parentMenuId")
    @Column(name = "parent_menu_id")
    private Long parentMenuId;

    @JsonSetter("parentMenuId")
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

    @Override
    public String underscoreName() {
        return Menu.unique_name;
    }
}
