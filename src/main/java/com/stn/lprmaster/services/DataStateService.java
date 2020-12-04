package com.stn.lprmaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stn.ester.core.exceptions.NotFoundException;
import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.DataState;
import com.stn.lprmaster.entities.dto.LastStateDTO;
import com.stn.lprmaster.entities.enumerate.DataStateStatus;
import com.stn.lprmaster.repositories.DataStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataStateService extends CrudService<DataState, DataStateRepository> {
    private DataStateRepository dataStateRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public DataStateService(DataStateRepository dataStateRepository) {
        super(dataStateRepository);
        this.dataStateRepository = dataStateRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Map<DataStateStatus, String> getDataStateStatus() {
        return DataStateStatus.toList().stream().collect(Collectors.toMap(status -> status, DataStateStatus::getLabel));
    }

    public LastStateDTO getDataLastStateByIdGate(String idGate) throws JsonProcessingException {
        Optional<DataState> dataStateOptional = dataStateRepository.findFirstByIdGate(idGate);
        if (!dataStateOptional.isPresent()) {
            throw new NotFoundException(String.format("ID Gate %s does not exists.", idGate));
        }
        DataState dataState = dataStateOptional.get();
        LastStateDTO.LastState lastState = objectMapper.readValue(dataState.getLastState(), LastStateDTO.LastState.class);
        return new LastStateDTO(dataState, lastState);
    }
}
