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
public class Department extends AppDomain {
    public static String unique_name = "department";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;
    private String label;

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Department> subDepartment = new HashSet<Department>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_department_id", insertable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Department parentDepartment;

    @JsonProperty("parentDepartmentId")
    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @JsonSetter("parentDepartmentId")
    public void setParentDepartmentId(long parentDepartmentId) {
        if (parentDepartmentId != 0)
            this.parentDepartmentId = parentDepartmentId;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void mergeSubDepartment(List<Department> subDepartment) {
        this.subDepartment.addAll(subDepartment);
    }

    public String getName() {
        return this.name;
    }

    public Department getParentDepartment() {
        return this.parentDepartment;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
