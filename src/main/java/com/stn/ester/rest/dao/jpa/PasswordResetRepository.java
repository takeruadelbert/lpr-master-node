package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.PasswordReset;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends AppRepository<PasswordReset, IdList> {
    PasswordReset findByToken(String token);

    PasswordReset findByUserId(Long id);
}
