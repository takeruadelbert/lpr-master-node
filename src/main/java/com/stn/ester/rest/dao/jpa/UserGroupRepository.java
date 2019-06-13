package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.UserGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends AppRepository<UserGroup, Long, IdList> {

    UserGroup findByName(String name);
}
