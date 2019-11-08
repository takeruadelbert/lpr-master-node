package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stn.ester.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
public class RoleGroup extends BaseEntity {

    private static final String COLUMN_ROLE_ID = "role_id";
    private static final String JSON_PROPERTY_ROLE = "roleId";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String JSON_PROPERTY_USER = "userId";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_USER_ID)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_ROLE_ID)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Role role;
}
