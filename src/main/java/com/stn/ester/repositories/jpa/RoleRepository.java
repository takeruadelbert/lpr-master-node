package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.Role;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelOption;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role>, RepositoryListTrait<IdLabelOption> {

    Role findByName(String name);
}
