package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.domain.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends AppRepository<Position, IdLabelList> {
    List<Position> findAllByParentPositionId(Long parentPositionId);
}
