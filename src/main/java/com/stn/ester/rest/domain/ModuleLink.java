package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
public class ModuleLink extends AppDomain {

    public static final String unique_name="module_link";

    private String name;
    private String alias;

    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Module module;

    @JsonIgnore
    public Module getModule() {
        return module;
    }

    @JsonIgnore
    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String underscoreName() {
        return ModuleLink.unique_name;
    }
}
