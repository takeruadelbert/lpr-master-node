package com.stn.ester.dto.MenuAndAccess;

import com.stn.ester.entities.Menu;
import lombok.Data;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class MenuDTO {
    private Long id;
    private String label;
    private int orderingNumber;
    private AccessGroupDTO accessGroup;
    private SortedSet<MenuDTO> subMenus = new TreeSet<>(Comparator.comparing(MenuDTO::getOrderingNumber));

    public MenuDTO(Menu menu){
        this.id=menu.getId();
        this.label=menu.getLabel();
        this.orderingNumber=menu.getOrderingNumber();
    }
}
