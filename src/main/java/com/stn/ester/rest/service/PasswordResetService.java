package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.PasswordResetRepository;
import com.stn.ester.rest.domain.PasswordReset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService extends AppService {

    @Autowired
    public PasswordResetService(PasswordResetRepository passwordResetRepository) {
        super(PasswordReset.unique_name);
        super.repositories.put(PasswordReset.unique_name, passwordResetRepository);
    }
}
