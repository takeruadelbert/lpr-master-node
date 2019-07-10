package com.stn.ester.rest.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.stn.ester.rest.domain.AccessLog;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.filter.wrapper.RequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RequestLoggingFilter extends OncePerRequestFilter {
    private Gson gson = new Gson();
    private ObjectMapper mapper = new ObjectMapper();
    public static AccessLog accessLog;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // make copy request of Input Stream
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ServletRequest copiedRequest = requestWrapper;

        String IPAdress_client = request.getHeader("X-FORWARDED-FOR");
        String URI = request.getRequestURI();
        String request_method = request.getMethod();
        Long user_id = SessionHelper.getUserID();
        if (IPAdress_client == null || "".equals(IPAdress_client)) {
            IPAdress_client = request.getRemoteAddr();
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request_method);
        String requestBody;

        // get request body
        try {
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            Map<String, Object> jsonMap = mapper.readValue(copiedRequest.getInputStream(), Map.class);
            requestBody = this.gson.toJson(jsonMap);
        } catch (MismatchedInputException ex) {
            requestBody = null;
        }

        accessLog = new AccessLog(IPAdress_client, URI, requestMethod, user_id, requestBody);

        chain.doFilter(copiedRequest, response);
    }
}
