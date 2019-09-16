package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelOption;
import com.stn.ester.entities.UserGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends BaseRepository<UserGroup>, RepositoryListTrait<IdLabelOption> {

    UserGroup findByName(String name);
}
