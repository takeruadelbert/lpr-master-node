package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import com.stn.ester.rest.domain.constant.EntityConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Department extends AppDomain {
    public static String unique_name = "department";
    private static final String COLUMN_PARENT_DEPARTMENT = "parent_department_id";
    private static final String JSON_PROPERTY_PARENT_DEPARTMENT = "parentDepartmentId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String name;
    private String label;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_PARENT_DEPARTMENT, insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Department parentDepartment;

    @JsonProperty(JSON_PROPERTY_PARENT_DEPARTMENT)
    @Column(name = COLUMN_PARENT_DEPARTMENT)
    private Long parentDepartmentId;

    @JsonSetter(JSON_PROPERTY_PARENT_DEPARTMENT)
    public void setParentDepartmentId(long parentDepartmentId) {
        if (parentDepartmentId != 0)
            this.parentDepartmentId = parentDepartmentId;
    }

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Department> subDepartment = new HashSet<Department>();

    public void mergeSubDepartment(List<Department> subDepartment) {
        this.subDepartment.addAll(subDepartment);
    }

    public String getName() {
        return this.name;
    }

    public Department getParentDepartment() {
        return this.parentDepartment;
    }

    public Long getParentDepartmentId() {return this.parentDepartmentId;}

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
