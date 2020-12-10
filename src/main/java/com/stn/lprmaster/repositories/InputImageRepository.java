package com.stn.lprmaster.repositories;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.lprmaster.entities.InputImage;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InputImageRepository extends BaseRepository<InputImage> {
    Optional<InputImage> findFirstByTicketNumber(String ticketNumber);
}
