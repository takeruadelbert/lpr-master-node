package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.News;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends BaseRepository<News> {

}
