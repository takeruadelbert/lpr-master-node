package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelList;
import com.stn.ester.entities.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends AppRepository<Position>, RepositoryListTrait<IdLabelList> {
    List<Position> findAllByParentPositionId(Long parentPositionId);
}
