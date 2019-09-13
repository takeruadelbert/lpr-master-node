package com.stn.ester.services.crud;

import com.stn.ester.entities.State;
import com.stn.ester.repositories.jpa.StateRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class StateService extends CrudService {
    private StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        super(State.class, stateRepository);
        super.repositories.put(State.class.getName(), stateRepository);
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