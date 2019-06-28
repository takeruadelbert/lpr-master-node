package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.domain.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepositoty extends AppRepository<Department, IdLabelList> {
    List<Department> findAllByParentDepartmentId(Long parentDepartmentId);

    List<Department> findAllByParentDepartmentIdIsNull();
}
