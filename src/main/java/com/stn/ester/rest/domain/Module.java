package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    public static final String unique_name="module";

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Alias is mandatory.")
    private String alias;

    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private Set<ModuleLink> moduleLink =  new HashSet<>();

    @Override
    public String underscoreName() {
        return Module.unique_name;
    }
}
