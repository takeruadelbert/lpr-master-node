package com.stn.ester.core.configurations;

import com.stn.ester.core.interceptors.AuthInterceptor;
import com.stn.ester.core.interceptors.DisabledPageInterceptor;
import com.stn.ester.core.interceptors.LicenseCheckInterceptor;
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
    private LicenseCheckInterceptor licenseCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(licenseCheckInterceptor);
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(disabledPageInterceptor);
    }
}
