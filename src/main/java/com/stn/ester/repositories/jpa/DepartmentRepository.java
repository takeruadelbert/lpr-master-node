package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelList;
import com.stn.ester.entities.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends AppRepository<Department>, RepositoryListTrait<IdLabelList> {
    List<Department> findAllByParentDepartmentId(Long parentDepartmentId);

    List<Department> findAllByParentDepartmentIdIsNull();
}
