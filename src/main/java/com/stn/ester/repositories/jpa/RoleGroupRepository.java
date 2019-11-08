package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.RoleGroup;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RoleGroupRepository extends BaseRepository<RoleGroup> {

    Collection<RoleGroup> findAllByRoleId(Long roleId);

    Collection<RoleGroup> findAllByRoleIdIn(Collection<Long> roleIds);

    Optional<RoleGroup> findByRoleIdAndUserId(Long roleId, Long userId);

    Collection<RoleGroup> findAllByUserId(Long userId);
}
