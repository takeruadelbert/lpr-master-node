package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.DepartmentRepositoty;
import com.stn.ester.rest.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentService extends AppService {
    private DepartmentRepositoty departmentRepositoty;

    @Autowired
    public DepartmentService(DepartmentRepositoty departmentRepositoty) {
        super(Department.unique_name);
        super.repositories.put(Department.unique_name, departmentRepositoty);
        this.departmentRepositoty = departmentRepositoty;
    }

    @Override
    public Object get(Long id) {
        if (repositories.get(baseRepoName).existsById(id)) {
            Object o = this.departmentRepositoty.findById(id).get();
            List<Department> subDepartments = this.findSubDepartment(id);
            ((Department) o).mergeSubDepartment(subDepartments);
            return o;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private List<Department> findSubDepartment(Long parentId) {
        return this.findSubDepartment(parentId, null);
    }

    private List<Department> findSubDepartment(Long parentId, Set<Long> departmentIds) {
        List<Department> subDepartments = this.departmentRepositoty.findAllByParentDepartmentId(parentId);
        if (!subDepartments.isEmpty()) {
            Iterator<Department> subDepartmentIterator = subDepartments.iterator();
            while (subDepartmentIterator.hasNext()) {
                Department department = subDepartmentIterator.next();
                if (departmentIds != null && !departmentIds.contains(department.getId())) {
                    subDepartmentIterator.remove();
                    continue;
                }
                List<Department> subDepartmentIt = this.findSubDepartment(department.getId(), departmentIds);
                int n = subDepartments.indexOf(department);
                Department mergedDepartment = subDepartments.get(n);
                mergedDepartment.mergeSubDepartment(subDepartmentIt);
                subDepartments.set(n, mergedDepartment);
            }
        }
        return subDepartments;
    }
}
