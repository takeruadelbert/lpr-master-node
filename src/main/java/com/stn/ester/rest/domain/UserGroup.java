package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class UserGroup extends AppDomain {

    public static final String unique_name="user_group";

    private String label;

    @Column(unique = true)
    private String name;

    @Transient
    private Set<AccessGroup> accessGroups=new HashSet<AccessGroup>();

    public Set<AccessGroup> mergeAccessGroup(Set<AccessGroup> accessGroups){
        this.accessGroups.addAll(accessGroups);
        return this.accessGroups;
    }

    @Override
    public String underscoreName() {
        return UserGroup.unique_name;
    }
}
