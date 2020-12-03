package com.stn.lprmaster.repositories;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.lprmaster.entities.DataState;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataStateRepository extends BaseRepository<DataState> {
    Optional<DataState> findFirstByIdGate(String idGate);
}
