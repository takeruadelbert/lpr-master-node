package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelOption;
import com.stn.ester.entities.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends BaseRepository<Position>, RepositoryListTrait<IdLabelOption> {
    List<Position> findAllByParentPositionId(Long parentPositionId);
}
