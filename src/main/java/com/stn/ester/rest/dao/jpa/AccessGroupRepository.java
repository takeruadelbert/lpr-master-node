package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.AccessGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccessGroupRepository extends AppRepository<AccessGroup, IdList> {

    Collection<AccessGroup> findAllByUserGroupId(Long userGroupId);

    Collection<AccessGroup> findAllByUserGroupIdAndViewable(Long userGroupId, boolean viewable);

    Collection<AccessGroup> findAllByMenuId(Long menuId);

    Collection<AccessGroup> findAllByMenuIdAndUserGroupId(Long menuId,Long userGroupId);

    Optional<AccessGroup> findByMenuIdAndUserGroupId(Long menuId,Long userGroupId);
}
