package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.StateRepository;
import com.stn.ester.rest.domain.State;
import org.springframework.stereotype.Service;

@Service
public class StateService extends AppService {
    public StateService(StateRepository stateRepository) {
        super(State.unique_name);
        super.repositories.put(State.unique_name, stateRepository);
    }
}
