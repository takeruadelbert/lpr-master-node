package com.stn.ester.core.configurations;

import com.stn.ester.core.interceptors.AccessLogInterceptor;
import com.stn.ester.core.interceptors.AuthInterceptor;
import com.stn.ester.core.interceptors.DisabledPageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private AccessLogInterceptor accessLogInterceptor;

    @Value("${ester.logging.access.enabled}")
    public static boolean accessLogEnabled;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(disabledPageInterceptor);
        if (accessLogEnabled)
            registry.addInterceptor(accessLogInterceptor);
    }
}
