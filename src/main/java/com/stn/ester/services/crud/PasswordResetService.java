package com.stn.ester.services.crud;

import com.stn.ester.entities.PasswordReset;
import com.stn.ester.repositories.jpa.PasswordResetRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService extends CrudService {

    @Autowired
    public PasswordResetService(PasswordResetRepository passwordResetRepository) {
        super(passwordResetRepository);
    }
}
