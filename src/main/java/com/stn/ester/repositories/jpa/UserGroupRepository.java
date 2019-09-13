package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelList;
import com.stn.ester.entities.UserGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends AppRepository<UserGroup>, RepositoryListTrait<IdLabelList> {

    UserGroup findByName(String name);
}
