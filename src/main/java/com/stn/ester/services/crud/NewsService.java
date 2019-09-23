package com.stn.ester.services.crud;

import com.stn.ester.entities.AssetFile;
import com.stn.ester.entities.News;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.enumerate.NewsStatus;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.helpers.SearchAndFilterHelper;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.repositories.jpa.NewsRepository;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.AssetFileClaimTrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService extends CrudService<News, NewsRepository> implements AssetFileClaimTrait {
    private NewsRepository newsRepository;
    private AssetFileService assetFileService;
    private String asset_path = "news";

    @Autowired
    public NewsService(NewsRepository newsRepository, AssetFileService assetFileService) {
        super(newsRepository);
        this.newsRepository = newsRepository;
        this.assetFileService = assetFileService;
    }

    @Transactional
    @Override
    public News create(News domain) {
        News news = setDataNews(domain);
        news.setNewsStatus(NewsStatus.SHOWED);
        return super.create(news);
    }

    @Transactional
    @Override
    public News update(Long id, News domain) {
        News news = setDataNews(domain);
        return super.update(id, news);
    }

    private News setDataNews(BaseEntity domain) {
        Long author_id = SessionHelper.getUserID();
        News news = ((News) domain);
        news.setAuthorId(author_id);
        String token = news.getToken();
        if (token != null && !token.isEmpty()) {
            AssetFile assetFile = claimFile(token);
            news.setThumbnailId(assetFile.getId());
            news.setThumbnail(assetFile);
        }
        return news;
    }

    public Page<News> dashboard(Integer page, Integer size) throws Exception {
        Long departmentId = SessionHelper.getDepartmentID();
        Map<String, Object> searchKeyValue = new HashMap<>();
        searchKeyValue.put("<:startDate", DateTimeHelper.getCurrentLocalDate());
        searchKeyValue.put(">:expiredDate", DateTimeHelper.getCurrentLocalDate());
        searchKeyValue.put(":newsStatus", NewsStatus.SHOWED);
        if (departmentId != null) {
            searchKeyValue.put(":departmentId", departmentId);
        }
        return super.index(page, size, SearchAndFilterHelper.resolveSpecification(searchKeyValue));
    }

    @Override
    public String getAssetPath() {
        return this.asset_path;
    }

    @Override
    public AssetFile claimFile(String token) {
        return this.assetFileService.moveTempDirToPermanentDir(token, this.getAssetPath());
    }

    public Map<NewsStatus, String> getStatusList() {
        Map<NewsStatus, String> result = new HashMap<>();
        List<NewsStatus> newsStatuses = NewsStatus.toList();
        for (NewsStatus newsStatus : newsStatuses) {
            result.put(newsStatus, newsStatus.getLabel());
        }
        return result;
    }

    public Object changeNewsStatus(Long newsId, NewsStatus newsStatus) {
        Map<String, Object> result = new HashMap<>();
        int code;
        String message;
        HttpStatus status;
        if (newsId != null && newsStatus != null) {
            News news = this.newsRepository.findById(newsId).get();
            news.setNewsStatus(newsStatus);
            super.update(newsId, news);
            code = HttpStatus.OK.value();
            message = "Successfully changed status.";
            status = HttpStatus.OK;
        } else {
            code = HttpStatus.BAD_REQUEST.value();
            message = "Failed to change status : invalid news ID and/or news status.";
            status = HttpStatus.BAD_REQUEST;
        }
        result.put("code", code);
        result.put("message", message);
        return new ResponseEntity<>(result, status);
    }
}
