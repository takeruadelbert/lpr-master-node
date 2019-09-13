package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.NewsRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.domain.News;
import com.stn.ester.rest.domain.enumerate.NewsStatus;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
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
public class NewsService extends AppService implements AssetFileBehaviour {
    private NewsRepository newsRepository;
    private AssetFileService assetFileService;
    private String asset_path = "news";

    @Autowired
    public NewsService(NewsRepository newsRepository, AssetFileService assetFileService) {
        super(News.unique_name);
        super.repositories.put(News.unique_name, newsRepository);
        this.newsRepository = newsRepository;
        this.assetFileService = assetFileService;
    }

    @Transactional
    @Override
    public Object create(AppDomain appDomain) {
        News news = ((News) appDomain);
        long author_id = SessionHelper.getUserID();
        news.setAuthorId(author_id);
        news.setNewsStatus(NewsStatus.SHOWED); // default option is showed.
        String token = news.getToken();
        if (token != null) {
            news.setThumbnailId(this.claimFile(token).getId());
        }
        return super.create(news);
    }

    @Override
    public Object update(Long id, AppDomain appDomain) {
        News news = ((News) appDomain);
        long author_id = SessionHelper.getUserID();
        news.setAuthorId(author_id);
        String token = news.getToken();
        if (token != null) {
            AssetFile thumbnail = this.claimFile(token);
            news.setThumbnailId(thumbnail.getId());
            news.setThumbnail(thumbnail);
        }
        return super.update(id, news);
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
