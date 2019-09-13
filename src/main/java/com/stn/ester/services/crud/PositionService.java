package com.stn.ester.services.crud;

import com.stn.ester.entities.Position;
import com.stn.ester.repositories.jpa.PositionRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class PositionService extends CrudService<Position, PositionRepository> {
    private PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        super(Position.class, positionRepository);
        super.repositories.put(Position.class.getName(), positionRepository);
        this.positionRepository = positionRepository;
    }

    @Override
    public Position get(Long id) {
        if (currentEntityRepository.existsById(id)) {
            Position o = this.positionRepository.findById(id).get();
            List<Position> subPositions = this.findSubPosition(id);
            o.mergeSubPosition(subPositions);
            return o;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private List<Position> findSubPosition(Long parentId) {
        return this.findSubPosition(parentId, null);
    }

    private List<Position> findSubPosition(Long parentId, Set<Long> positionIds) {
        List<Position> subPositions = this.positionRepository.findAllByParentPositionId(parentId);
        if (!subPositions.isEmpty()) {
            Iterator<Position> subPositionIterator = subPositions.iterator();
            while (subPositionIterator.hasNext()) {
                Position position = subPositionIterator.next();
                if (positionIds != null && !positionIds.contains(position.getId())) {
                    subPositionIterator.remove();
                    continue;
                }
                List<Position> subPositionIt = this.findSubPosition(position.getId(), positionIds);
                int n = subPositions.indexOf(position);
                Position mergedPosition = subPositions.get(n);
                mergedPosition.mergeSubPosition(subPositionIt);
                subPositions.set(n, mergedPosition);
            }
        }
        return subPositions;
    }
}
