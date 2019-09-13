package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends AppRepository<News> {

}
