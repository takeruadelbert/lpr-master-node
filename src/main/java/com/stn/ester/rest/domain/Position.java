package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Position extends AppDomain {
    public static String unique_name = "position";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;
    private String label;

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Position> subPosition = new HashSet<Position>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_position_id", insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Department parentPosition;

    @JsonProperty("parentPositionId")
    @Column(name = "parent_position_id")
    private Long parentPositionId;

    @JsonSetter("parentPositionId")
    public void setParentPositionId(long parentPositionId) {
        if (parentPositionId != 0)
            this.parentPositionId = parentPositionId;
    }

    public Long getParentPositionId() {
        return parentPositionId;
    }

    public void mergeSubPosition(List<Position> subPosition) {
        this.subPosition.addAll(subPosition);
    }

    public String underscoreName() {
        return unique_name;
    }
}
