package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.domain.Biodata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiodataService extends AppService{

    @Autowired
    public BiodataService(BiodataRepository biodataRepository){
        super(Biodata.unique_name);
        super.repositories.put(Biodata.unique_name,biodataRepository);
    }

}
