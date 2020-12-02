package com.stn.ester.services;

import com.stn.ester.entities.EsterLicense;
import com.stn.ester.repositories.jpa.EsterLicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EsterLicenseService {

    private EsterLicenseRepository esterLicenseRepository;

    @Autowired
    public EsterLicenseService(EsterLicenseRepository esterLicenseRepository) {
        this.esterLicenseRepository = esterLicenseRepository;
    }

    public EsterLicense getOrCreate() {
        EsterLicense esterLicense = esterLicenseRepository.findFirstByIdIsNotNull();
        if (esterLicense != null) {
            return esterLicense;
        } else {
            EsterLicense newEsterLicense = new EsterLicense();
            newEsterLicense.setExpire(LocalDate.now().plusYears(1));
            newEsterLicense.setCheckExpire(false);
            newEsterLicense.setLicenseTo("PT. Surya Teknologi Nasional");
            return this.esterLicenseRepository.save(newEsterLicense);
        }
    }

    public Boolean isExpired() {
        EsterLicense esterLicense = this.getOrCreate();
        if (esterLicense.getCheckExpire() && LocalDate.now().isAfter(esterLicense.getExpire())) {
            return true;
        }
        return false;
    }
}
