package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.FileRepository;
import com.stn.ester.rest.domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService extends AppService{

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository){
        super(File.unique_name);
        super.repositories.put(File.unique_name, fileRepository);
    }

    public File Create(File voData) {
        return fileRepository.save(voData);
    }
}
