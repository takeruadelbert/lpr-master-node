package com.stn.ester.services.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stn.ester.entities.Department;
import com.stn.ester.repositories.jpa.DepartmentRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentService extends CrudService<Department, DepartmentRepository>{
    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        super(Department.class, departmentRepository);
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department get(Long id) {
        if (currentEntityRepository.existsById(id)) {
            Department department = currentEntityRepository.findById(id).get();
            List<Department> subDepartments = this.findSubDepartment(id);
            department.mergeSubDepartment(subDepartments);
            return department;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private List<Department> findSubDepartment(Long parentId) {
        return this.findSubDepartment(parentId, null);
    }

    private List<Department> findSubDepartment(Long parentId, Set<Long> departmentIds) {
        List<Department> subDepartments = currentEntityRepository.findAllByParentDepartmentId(parentId);
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

    public Object getDepartmentAndSubDepartmentGroupList() {
        Map<String, Object> result = new HashMap<>();
        List<Department> departments = currentEntityRepository.findAllByParentDepartmentIdIsNull();
        Map<String, Object> temp = new HashMap<>();
        toOptionGroup(departments, result, temp);
        return result;
    }

    public void toOptionGroup(List<Department> departments, Map<String, Object> result, Map<String, Object> temp) {
        for (Department department : departments) {
            System.out.println("Departemen = " + department.getName());
            if (!this.findSubDepartment(department.getId()).isEmpty()) {
                if (department.getParentDepartmentId() != null) {
                    if (department.getParentDepartment().getParentDepartmentId() == null) {
                        temp = new HashMap<>();
                    }
                    System.out.println("aaa");
                    temp.put(department.getId() + "", department.getName());
                    System.out.println("temp = " + temp);
                    System.out.println("result before = " + result);
                    if (result.get(department.getParentDepartment().getName()) != "" && result.get(department.getParentDepartment().getName()) != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) (result.get(department.getParentDepartment().getName()))).entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            System.out.println("key = " + key + ", value = " + value);
                            temp.put(key, value);
                        }
                    }
                    result.put(department.getParentDepartment().getName(), temp);
                    System.out.println("result = " + result);
                } else {
                    System.out.println("ccc");
                    result.put(department.getName(), "");
                    System.out.println("result(1) = " + result);
                }
                temp = new HashMap<>();
                toOptionGroup(this.findSubDepartment(department.getId()), result, temp);

            } else {
                if (department.getParentDepartmentId() != null) {
                    System.out.println("bbb");
                    temp.put(department.getId() + "", department.getName());
                    System.out.println("temp2 = " + temp);
                    result.put(department.getParentDepartment().getName(), temp);
                    System.out.println("result2 = " + result);
                }
            }
        }
    }
}
