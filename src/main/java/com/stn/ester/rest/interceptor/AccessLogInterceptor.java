package com.stn.ester.rest.interceptor;

import com.stn.ester.rest.dao.jpa.AccessLogRepository;
import com.stn.ester.rest.domain.AccessLog;
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
    public static ThreadLocal<Long> accessLogId = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return -1L;
        }
    };;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (RequestLoggingFilter.accessLog != null) {
            AccessLog dataAccessLog = this.accessLogRepository.save(RequestLoggingFilter.accessLog);
            accessLogId.set(dataAccessLog.getId());
        }
        return super.preHandle(request, response, handler);
    }
}
