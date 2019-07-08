package com.stn.ester.rest.config;

import com.stn.ester.rest.interceptor.AccessInterceptor;
import com.stn.ester.rest.interceptor.AuthInterceptor;
import com.stn.ester.rest.interceptor.DisabledPageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private DisabledPageInterceptor disabledPageInterceptor;

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(disabledPageInterceptor);
        registry.addInterceptor(accessInterceptor);
    }
}
