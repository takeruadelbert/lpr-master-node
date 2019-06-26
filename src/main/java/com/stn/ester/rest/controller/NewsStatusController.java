package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.NewsStatus;
import com.stn.ester.rest.service.NewsStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news_statuses")
public class NewsStatusController extends AppController<NewsStatusService, NewsStatus> {
    @Autowired
    public NewsStatusController(NewsStatusService newsStatusService) {
        super(newsStatusService);
    }
}
