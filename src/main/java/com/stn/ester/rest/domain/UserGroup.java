package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class UserGroup extends AppDomain {

    public static final String unique_name="user_group";

    @NotBlank(message = "Label is mandatory.")
    private String label;

    @NotBlank(message = "Name is mandatory.")
    @Column(unique = true)
    private String name;

    @Transient
    private Collection<AccessGroup> accessGroups=new HashSet<AccessGroup>();

    public Collection<AccessGroup> mergeAccessGroup(Collection<AccessGroup> accessGroups){
        this.accessGroups.addAll(accessGroups);
        return this.accessGroups;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AccessGroup> getAccessGroups() {
        return accessGroups;
    }

    @Override
    public String underscoreName() {
        return UserGroup.unique_name;
    }
}
