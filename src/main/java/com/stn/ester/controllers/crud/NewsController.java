package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.entities.News;
import com.stn.ester.entities.enumerate.NewsStatus;
import com.stn.ester.services.crud.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Object update(@PathVariable long id, @Valid @RequestBody News news, @RequestBody Map<String, Object> requestBody) {
        return service.update(id, news);
    }

    @RequireLogin
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public Page<News> dashboard(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = "2") Integer size) throws Exception {
        return service.dashboard(page, size);
    }

    @RequireLogin
    @RequestMapping(value = "/status", method = RequestMethod.OPTIONS)
    public Map<NewsStatus, String> getStatusList() {
        return service.getStatusList();
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('changeNewsStatus'))")
    @RequestMapping(value = "/{id}/status/change", method = RequestMethod.PUT)
    public Object changeNewsStatus(@PathVariable Long id, @RequestBody Map<String, String> data) {
        String dataNewsStatus = data.get("status");
        NewsStatus newsStatus = NewsStatus.valueOf(dataNewsStatus);
        return service.changeNewsStatus(id, newsStatus);
    }
}
