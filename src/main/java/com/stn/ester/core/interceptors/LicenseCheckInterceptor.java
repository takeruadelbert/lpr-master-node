package com.stn.ester.core.interceptors;

import com.stn.ester.core.exceptions.LicenseExpiredException;
import com.stn.ester.services.EsterLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    EsterLicenseService esterLicenseService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentRequest = request.getMethod() + " " + request.getRequestURI();
        System.out.println(currentRequest);
        if (handler instanceof HandlerMethod) {
            if (esterLicenseService.isExpired() &&
                    !currentRequest.equals("GET /status") &&
                    !currentRequest.equals("GET /system_profiles") &&
                    !currentRequest.equals("GET /license")
            ) {
                throw new LicenseExpiredException();
            }
        }
        return super.preHandle(request, response, handler);
    }
}
