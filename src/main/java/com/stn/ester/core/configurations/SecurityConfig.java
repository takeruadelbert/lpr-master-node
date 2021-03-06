package com.stn.ester.core.configurations;

import com.stn.ester.core.base.auth.AccessAllowed;
import com.stn.ester.core.security.JWTAuthenticationFilter;
import com.stn.ester.core.security.JWTAuthorizationFilter;
import com.stn.ester.helpers.GlobalFunctionHelper;
import com.stn.ester.services.AuthenticationService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        for (PathMethod pathMethod : findPathAnnotatedAccessAllowed()) {
            web.ignoring().antMatchers(HttpMethod.resolve(pathMethod.requestMethod.toString()), pathMethod.Path);
        }
        web.ignoring().antMatchers("/error");
        web.ignoring().antMatchers("/ws");
        web.ignoring().antMatchers("/ws/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), authenticationService, eventPublisher))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), authenticationService, userService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    private List<PathMethod> findPathAnnotatedAccessAllowed() {
        ApplicationContext applicationContext = super.getApplicationContext();
        List<PathMethod> result = new ArrayList<>();
        for (Map.Entry<String, Object> entryBean : applicationContext.getBeansWithAnnotation(RestController.class).entrySet()) {
            /*
             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
             * Proxy use java.lang.reflect.Proxy#isProxyClass
             */
            Class<?> objClz = entryBean.getValue().getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(entryBean.getValue())) {

                objClz = org.springframework.aop.support.AopUtils.getTargetClass(entryBean.getValue());
            }
            String topPath = "";
            if (objClz.getAnnotation(RequestMapping.class) != null) {
                String[] classPaths = objClz.getAnnotation(RequestMapping.class).value();
                if (classPaths.length != 0) {
                    topPath = classPaths[0];
                }
            }
            for (Method method : objClz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(AccessAllowed.class)) {
                    String[] paths = method.getAnnotation(RequestMapping.class).value();
                    RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
                    for (String path : paths) {
                        for (RequestMethod requestMethod : requestMethods) {
                            result.add(new PathMethod(requestMethod, GlobalFunctionHelper.replacePathVariableTo(topPath + path, "**")));
                        }
                    }
                }
            }
        }
        return result;
    }

    class PathMethod {
        RequestMethod requestMethod;
        String Path;

        public PathMethod(RequestMethod requestMethod, String path) {
            this.requestMethod = requestMethod;
            Path = path;
        }

        @Override
        public String toString() {
            return "PathMethod{" +
                    "requestMethod=" + requestMethod +
                    ", Path='" + Path + '\'' +
                    '}';
        }
    }
}