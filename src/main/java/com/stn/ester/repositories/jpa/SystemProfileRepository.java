package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.SystemProfile;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemProfileRepository extends BaseRepository<SystemProfile> {
    SystemProfile findFirstByIdIsNotNull();
}
