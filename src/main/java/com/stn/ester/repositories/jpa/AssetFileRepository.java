package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.entities.AssetFile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetFileRepository extends BaseRepository<AssetFile> {
    Optional<AssetFile> findByNameAndExtension(String name, String ext);

    Optional<AssetFile> findByToken(String token);
}