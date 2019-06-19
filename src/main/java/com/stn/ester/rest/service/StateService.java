package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.StateRepository;
import com.stn.ester.rest.domain.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StateService extends AppService {
    private StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        super(State.unique_name);
        super.repositories.put(State.unique_name, stateRepository);
        this.stateRepository = stateRepository;
    }

    public Object getStateListByCountry(long country_id) {
        List<State> states = this.stateRepository.findAllByCountryId(country_id);
        HashMap<Long, String> result = new HashMap<>();
        if (!states.isEmpty()) {
            for (State state : states) {
                result.put(state.getId(), state.getName());
            }
        }
        return result;
    }
}