package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stn.ester.rest.domain.constant.EntityConstant;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Module extends AppDomain {

    public static final String unique_name = "module";
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

    @Override
    public String underscoreName() {
        return Module.unique_name;
    }
}
