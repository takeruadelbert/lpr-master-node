package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.NewsRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.News;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        long author_id = SessionHelper.getUserID();
        ((News) o).setAuthorId(author_id);
        ((News) o).setNewsStatusId(1); // default option is showed.
        return super.create(o);
    }

    @Override
    public Object update(Long id, AppDomain o) {
        long author_id = SessionHelper.getUserID();
        ((News) o).setAuthorId(author_id);
        return super.update(id, o);
    }

    public Object getAllValidNews() throws Exception {
        List<News> validNews = new ArrayList<>();
        Iterable<News> allNews = this.newsRepository.findAll();

        long department_id = SessionHelper.getDepartmentID();

        // get current date
        Date today = DateTimeHelper.getCurrentDate();

        for (News news : allNews) {
            // check if period date of news exists
            if (news.getStartDate() != null && news.getExpiredDate() != null) {
                // check if news still valid within period of date
                Date startDate = news.getStartDate();
                Date expiredDate = news.getExpiredDate();
                if (!startDate.after(today) && !expiredDate.before(today)) {
                    if (news.getDepartmentId() == null || news.getDepartmentId() == department_id)
                        validNews.add(news);
                }
            } else {
                // otherwise just get all news status id 1 -> showing
                if (news.getNewsStatusId() == 1) {
                    if (news.getDepartmentId() == null || news.getDepartmentId() == department_id)
                        validNews.add(news);
                }
            }
        }
        return validNews;
    }
}
