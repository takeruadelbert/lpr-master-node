package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.AccessGroup;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AccessGroupRepository extends AppRepository<AccessGroup> {

    Collection<AccessGroup> findAllByUserGroupId(Long userGroupId);

    Collection<AccessGroup> findAllByUserGroupIdAndViewable(Long userGroupId, boolean viewable);

    Collection<AccessGroup> findAllByMenuId(Long menuId);

    Collection<AccessGroup> findAllByMenuIdAndUserGroupId(Long menuId,Long userGroupId);

    Collection<AccessGroup> findAllByMenuIdInAndUserGroupId(Collection<Long> menuIds, Long userGroupId);


    Optional<AccessGroup> findByMenuIdAndUserGroupId(Long menuId,Long userGroupId);
}
