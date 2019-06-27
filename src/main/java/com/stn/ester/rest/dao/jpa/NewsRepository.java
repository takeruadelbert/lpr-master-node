package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends AppRepository<News, IdList> {
    List<News> findAllByNewsStatusId(long newsStatusId);
}
