package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.AccessGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccessGroupRepository extends AppRepository<AccessGroup, IdList> {

    Set<AccessGroup> findAllByUserGroupId(Long userGroupId);

    Set<AccessGroup> findAllByUserGroupIdAndViewable(Long userGroupId, boolean viewable);

    Iterable<AccessGroup> findAllByMenuId(Long menuId);
}
