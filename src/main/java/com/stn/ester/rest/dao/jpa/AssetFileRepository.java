package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.AssetFile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetFileRepository extends AppRepository<AssetFile, IdList> {
    Optional<AssetFile> findByNameAndExtension(String name, String ext);
}