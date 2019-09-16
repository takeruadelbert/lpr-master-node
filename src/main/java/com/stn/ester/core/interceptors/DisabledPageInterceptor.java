package com.stn.ester.core.interceptors;

import com.stn.ester.core.base.DisabledAccess;
import com.stn.ester.core.base.PageAccess;
import com.stn.ester.core.exceptions.DisabledAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Component
public class DisabledPageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Annotation disabledAccessAnnotation = handlerMethod.getBeanType().getDeclaredAnnotation(DisabledAccess.class);
            String methodName=handlerMethod.getMethod().getName();
            if (disabledAccessAnnotation != null) {
                PageAccess[] pageAccesses=handlerMethod.getBeanType().getDeclaredAnnotation(DisabledAccess.class).value();
                for (PageAccess pageAccess:pageAccesses){
                    if (pageAccess.isEqual(methodName)){
                        throw new DisabledAccessException();
                    }
                }
            }
        }
        return super.preHandle(request, response, handler);
    }
}
