package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.entities.PasswordReset;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends BaseRepository<PasswordReset> {
    PasswordReset findByToken(String token);

    PasswordReset findByUserId(Long id);
}
