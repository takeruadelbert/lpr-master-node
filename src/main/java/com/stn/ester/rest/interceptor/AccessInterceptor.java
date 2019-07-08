package com.stn.ester.rest.interceptor;

import com.stn.ester.rest.dao.jpa.AccessLogRepository;
import com.stn.ester.rest.domain.AccessLog;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.helper.SessionHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletRequest copiedRequest = new HttpServletRequestWrapper(request);
        String IPAdress_client = request.getHeader("X-FORWARDED-FOR");
        String URI = request.getRequestURI();
        String request_method = request.getMethod();
        Long user_id = SessionHelper.getUserID();
        if (IPAdress_client == null || "".equals(IPAdress_client)) {
            IPAdress_client = request.getRemoteAddr();
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request_method);
        String requestData = "";
//        if (request_method.equals("POST") || request_method.equals("PUT")) {
//            requestData = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
//            System.out.println("Request Body = " + requestData);
//        }
        AccessLog accessLog = new AccessLog(IPAdress_client, URI, requestMethod, user_id);
        this.accessLogRepository.save(accessLog);
        return super.preHandle(request, response, handler);
    }

    private String getBody(HttpServletRequest request) {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    System.out.println("Input Stream Closed");
                } catch (IOException ex) {
                    System.out.println("aaa");
                    ex.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    System.out.println("Buffered Reader CLOSED");
                } catch (IOException ex) {
                    System.out.println("bbb");
                    ex.printStackTrace();
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }
}
