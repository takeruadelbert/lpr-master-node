package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stn.ester.core.base.AutoRemoveChild;
import com.stn.ester.core.base.OnDeleteSetParentNull;
import com.stn.ester.core.base.TableFieldPair;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.services.crud.MenuService;
import com.stn.ester.services.crud.ModuleLinkService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AutoRemoveChild({
        @TableFieldPair(service = ModuleLinkService.class, attributeName = "moduleLink")
})
@OnDeleteSetParentNull({
        @TableFieldPair(service = MenuService.class, fieldName = "module_id")
})
public class Module extends BaseEntity {

    private static final String COLUMN_MAPPED_BY_MODULE = "module";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    private String name;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    private String alias;

    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_BY_MODULE, cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private Set<ModuleLink> moduleLink = new HashSet<>();

    public String getAlias() {
        return alias;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getName() {
        return name;
    }

}
