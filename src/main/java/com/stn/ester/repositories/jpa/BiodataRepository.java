package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.Biodata;
import org.springframework.stereotype.Repository;

@Repository
public interface BiodataRepository extends AppRepository<Biodata> {
}
