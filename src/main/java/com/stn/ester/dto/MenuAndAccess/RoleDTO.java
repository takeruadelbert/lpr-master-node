package com.stn.ester.dto.MenuAndAccess;

import com.stn.ester.entities.Role;
import lombok.Data;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class RoleDTO {

    private Long id;
    private String label;
    private String name;
    private SortedSet<MenuDTO> menus = new TreeSet<>(Comparator.comparing(MenuDTO::getOrderingNumber));

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.label = role.getLabel();
        this.name = role.getName();
    }

}
