package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends AppRepository<News, IdList> {
}
