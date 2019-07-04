package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
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

    private long id;

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;
    private String label;

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

    @Transient
    @EqualsAndHashCode.Exclude
    private Set<Department> subDepartment = new HashSet<Department>();

    public void mergeSubDepartment(List<Department> body) {
        this.subDepartment.addAll(body);
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"" + name + "\":" + subDepartment + "}";
    }
}
