package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.PasswordReset;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends AppRepository<PasswordReset> {
    PasswordReset findByToken(String token);

    PasswordReset findByUserId(Long id);
}
