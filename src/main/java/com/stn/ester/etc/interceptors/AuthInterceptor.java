package com.stn.ester.etc.interceptors;

import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private UserService userService;
    private ArrayList<String> nonSecurePage=new ArrayList();

    @Autowired
    AuthInterceptor(UserService userService){
        this.userService=userService;
        this.nonSecurePage.add("POST /users/login");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentRequest=request.getMethod()+" "+request.getRequestURI();
        System.out.println(currentRequest);
        String accessToken=request.getHeader("access-token");
        //this.userService.isValidToken(accessToken);
        return super.preHandle(request, response, handler);
    }
}
