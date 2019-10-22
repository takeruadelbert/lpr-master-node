package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.entities.AccessGroup;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AccessGroupRepository extends BaseRepository<AccessGroup> {

    Collection<AccessGroup> findAllByRoleId(Long roleId);

    Collection<AccessGroup> findAllByRoleIdAndViewable(Long roleId, boolean viewable);

    Collection<AccessGroup> findAllByRoleIdInAndViewable(Collection<Long> roleIds, boolean viewable);

    Collection<AccessGroup> findAllByMenuId(Long menuId);

    Collection<AccessGroup> findAllByMenuIdAndRoleId(Long menuId, Long roleId);

    Collection<AccessGroup> findAllByMenuIdInAndRoleId(Collection<Long> menuIds, Long roleId);

    Collection<AccessGroup> findAllByMenuIdInAndRoleIdIn(Collection<Long> menuIds, Collection<Long> roleIds);

    Optional<AccessGroup> findByMenuIdAndRoleId(Long menuId, Long roleId);

    Optional<AccessGroup> findByMenuIdAndRoleIdIn(Long menuId, Collection<Long> roleIds);
}
