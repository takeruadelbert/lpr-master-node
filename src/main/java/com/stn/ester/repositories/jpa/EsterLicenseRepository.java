package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.EsterLicense;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsterLicenseRepository extends BaseRepository<EsterLicense> {
    EsterLicense findFirstByIdIsNotNull();
}
