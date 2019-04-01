package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiodataService extends AppService{

    @Autowired
    public BiodataService(BiodataRepository biodataRepository){
        super("biodata");
        super.repositories.put("biodata",biodataRepository);
    }

}
