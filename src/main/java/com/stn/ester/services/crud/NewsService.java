package com.stn.ester.services.crud;

import com.stn.ester.entities.AssetFile;
import com.stn.ester.entities.News;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.enumerate.NewsStatus;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.repositories.jpa.NewsRepository;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.AssetFileClaimTrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public Page<News> dashboard(Integer page, Integer size, Pageable pageable) throws Exception {
        List<News> validNews = new ArrayList<>();
        Iterable<News> allNews = this.newsRepository.findAll();

        long department_id = SessionHelper.getDepartmentID();

        // get current date
        LocalDate today = LocalDate.now();

        if (allNews != null) {
            for (News news : allNews) {
                // check if period date of news exists
                if (news.getStartDate() != null && news.getExpiredDate() != null) {
                    // check if news still valid within period of date
                    LocalDate startDate = news.getStartDate();
                    LocalDate expiredDate = news.getExpiredDate();
                    if (!startDate.isAfter(today) && !expiredDate.isBefore(today)) {
                        if (news.getDepartmentId() == null || news.getDepartmentId() == department_id)
                            validNews.add(news);
                    }
                } else {
                    // otherwise just get all news status id 1 -> showing
                    if (news.getNewsStatus() == NewsStatus.SHOWED) {
                        if (news.getDepartmentId() == null || news.getDepartmentId() == department_id)
                            validNews.add(news);
                    }
                }
            }
        }
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > validNews.size() ? validNews.size() : (start + pageable.getPageSize());
        List<News> temp = start <= end ? validNews.subList(start, end) : new ArrayList<>();
        Page<News> newsDashboard = new PageImpl<>(temp, PageRequest.of(page, size), validNews.size());
        return newsDashboard;
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
