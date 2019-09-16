package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelOption;
import com.stn.ester.entities.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends BaseRepository<Department>, RepositoryListTrait<IdLabelOption> {
    List<Department> findAllByParentDepartmentId(Long parentDepartmentId);

    List<Department> findAllByParentDepartmentIdIsNull();
}
