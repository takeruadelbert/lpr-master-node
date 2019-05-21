package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.GenderRepository;
import com.stn.ester.rest.domain.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService extends AppService {

    @Autowired
    public GenderService(GenderRepository genderRepository) {
        super(Gender.unique_name);
        super.repositories.put(Gender.unique_name, genderRepository);
    }
}