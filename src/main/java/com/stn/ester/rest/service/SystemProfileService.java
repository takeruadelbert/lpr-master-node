package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.SystemProfileRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.SystemProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SystemProfileService extends AppService {
    private SystemProfileRepository systemProfileRepository;

    @Autowired
    public SystemProfileService(SystemProfileRepository systemProfileRepository) {
        super(SystemProfile.unique_name);
        super.repositories.put(SystemProfile.unique_name, systemProfileRepository);
        this.systemProfileRepository = systemProfileRepository;
    }

    @Transactional
    public Object updateSingleData(AppDomain object) {
        Long id = Long.valueOf(1);
        if(systemProfileRepository.existsById(id)) {
            object.setId(id);
            return super.update(id, object);
        } else {
            return super.create(object);
        }
    }
}
