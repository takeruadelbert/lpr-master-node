package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.SystemProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemProfileRepository extends AppRepository<SystemProfile> {
    SystemProfile findFirstByIdIsNotNull();
}
