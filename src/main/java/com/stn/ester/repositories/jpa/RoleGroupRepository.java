package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.RoleGroup;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RoleGroupRepository extends BaseRepository<RoleGroup> {

    Collection<RoleGroup> findAllByRoleId(Long roleId);

    Collection<RoleGroup> findAllByRoleIdIn(Collection<Long> roleIds);
}
