package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.NewsRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.News;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NewsService extends AppService {
    private NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        super(News.unique_name);
        super.repositories.put(News.unique_name, newsRepository);
        this.newsRepository = newsRepository;
    }

    @Transactional
    @Override
    public Object create(AppDomain o) {
        long user_id = SessionHelper.getUserID();
        ((News) o).setUserId(user_id);
        return super.create(o);
    }

    @Override
    public Object update(Long id, AppDomain o) {
        long user_id = SessionHelper.getUserID();
        ((News) o).setUserId(user_id);
        return super.update(id, o);
    }
}
