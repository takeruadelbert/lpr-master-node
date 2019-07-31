package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.News;
import com.stn.ester.rest.domain.enumerate.NewsStatus;
import com.stn.ester.rest.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/news")
public class NewsController extends CrudController<NewsService, News> {
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

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public Page<News> dashboard(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = "2") Integer size) throws Exception {
        return service.dashboard(page, size, PageRequest.of(page, size));
    }

    @RequestMapping(value = "/status", method = RequestMethod.OPTIONS)
    public Map<NewsStatus, String> getStatusList() {
        return service.getStatusList();
    }

    @RequestMapping(value = "/{id}/status/change", method = RequestMethod.PUT)
    public Object changeNewsStatus(@PathVariable Long id, @RequestBody Map<String, String> data) {
        String dataNewsStatus = data.get("status");
        NewsStatus newsStatus = NewsStatus.valueOf(dataNewsStatus);
        return service.changeNewsStatus(id, newsStatus);
    }
}
