package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.core.base.OnDeleteSetParentNull;
import com.stn.ester.core.base.TableFieldPair;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.services.crud.PositionService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@OnDeleteSetParentNull({
        @TableFieldPair(service = PositionService.class, fieldName = "parent_position_id")
})
public class Position extends BaseEntity {
    private static final String COLUMN_PARENT_POSITION = "parent_position_id";
    private static final String COLUMN_USER_GROUP = "user_group_id";
    private static final String JSON_PROPERTY_PARENT_POSITION = "parentPositionId";
    private static final String JSON_PROPERTY_USER_GROUP = "userGroupId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String name;
    private String label;

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Position> subPosition = new HashSet<Position>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_PARENT_POSITION, insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Position parentPosition;

    @JsonProperty(JSON_PROPERTY_PARENT_POSITION)
    @Column(name = COLUMN_PARENT_POSITION)
    private Long parentPositionId;

    @JsonSetter(JSON_PROPERTY_PARENT_POSITION)
    public void setParentPositionId(long parentPositionId) {
        if (parentPositionId != 0)
            this.parentPositionId = parentPositionId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_USER_GROUP, insertable = false, updatable = false)
    private Role role;

    @NotNull(message = EntityConstant.MESSAGE_NOT_BLANK)
    @JsonProperty(JSON_PROPERTY_USER_GROUP)
    @Column(name = COLUMN_USER_GROUP, nullable = false)
    private Long userGroupId;

    @JsonSetter(JSON_PROPERTY_USER_GROUP)
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }

    public Long getUserGroupId() {
        return this.userGroupId;
    }

    public Long getParentPositionId() {
        return parentPositionId;
    }

    public void mergeSubPosition(List<Position> subPosition) {
        this.subPosition.addAll(subPosition);
    }

}
