package com.stn.ester.dto.entity;

import com.stn.ester.dto.base.EntityDTO;
import com.stn.ester.entities.Menu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuSimpleDTO extends EntityDTO<Menu> {

    public Long id;
    public String label;
    public String moduleName;

    public MenuSimpleDTO(Menu menu) {
        super(menu);
        this.id = menu.getId();
        this.label = menu.getLabel();
        if (menu.getModule() != null) {
            this.moduleName = menu.getModule().getName();
        }
    }
}
