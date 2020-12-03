package com.stn.lprmaster.services;

import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.DataState;
import com.stn.lprmaster.entities.enumerate.DataStateStatus;
import com.stn.lprmaster.repositories.DataStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataStateService extends CrudService<DataState, DataStateRepository> {
    private DataStateRepository dataStateRepository;

    @Autowired
    public DataStateService(DataStateRepository dataStateRepository) {
        super(dataStateRepository);
        this.dataStateRepository = dataStateRepository;
    }

    public Map<DataStateStatus, String> getDataStateStatus() {
        return DataStateStatus.toList().stream().collect(Collectors.toMap(status -> status, DataStateStatus::getLabel));
    }
}
