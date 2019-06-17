package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.News;
import com.stn.ester.rest.service.NewsService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/news")
public class NewsController extends AppController<NewsService, News> {
    @Autowired
    public NewsController(NewsService newsService) {
        super(newsService);
    }

    @Override
    public Object create(@Valid @RequestBody News news) {
        return service.create(news);
    }

    @Override
    public Object update(@PathVariable long id, @Valid @RequestBody News news) {
        return service.update(id, news);
    }
}
