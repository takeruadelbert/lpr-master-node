package com.stn.ester.rest.interceptor;

import com.stn.ester.rest.dao.jpa.AccessLogRepository;
import com.stn.ester.rest.filter.RequestLoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessLogInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (RequestLoggingFilter.accessLog != null)
            this.accessLogRepository.save(RequestLoggingFilter.accessLog);
        return super.preHandle(request, response, handler);
    }
}
