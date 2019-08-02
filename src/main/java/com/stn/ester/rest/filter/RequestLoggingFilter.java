package com.stn.ester.rest.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.stn.ester.rest.domain.AccessLog;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.filter.wrapper.RequestWrapper;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RequestLoggingFilter extends OncePerRequestFilter {
    private Gson gson = new Gson();
    private ObjectMapper mapper = new ObjectMapper();
    public static AccessLog accessLog;
    private static String WHITESPACE = " ";
    private static final List<String> IGNORE_REQUEST_BODY_URL = Arrays.asList(
            RequestMethod.POST + WHITESPACE + "/users/login",
            RequestMethod.POST + WHITESPACE + "/asset_files/upload",
            RequestMethod.POST + WHITESPACE + "/asset_files/upload-encoded"
    );

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // make copy request of Input Stream
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ServletRequest copiedRequest = requestWrapper;

        String IPAdress_client = request.getHeader("X-FORWARDED-FOR");
        String queryParam = request.getQueryString();
        String URI = queryParam == null ? request.getRequestURI() : request.getRequestURI() + "?" + queryParam;
        String request_method = request.getMethod();
        Long user_id = SessionHelper.getUserID();
        if (IPAdress_client == null || "".equals(IPAdress_client)) {
            IPAdress_client = request.getRemoteAddr();
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request_method);
        String requestBody = null;

        if(!this.isExcludedFromList(request)) {
            // get request body
            try {
                mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
                Map<String, Object> jsonMap = mapper.readValue(copiedRequest.getInputStream(), Map.class);
                requestBody = this.gson.toJson(jsonMap);
            } catch (MismatchedInputException ex) {
                requestBody = null;
            }
        }
        accessLog = new AccessLog(IPAdress_client, URI, requestMethod, user_id, requestBody);

        chain.doFilter(copiedRequest, response);
    }

    private boolean isExcludedFromList(HttpServletRequest request) {
        String servletPath = RequestMethod.valueOf(request.getMethod()) + WHITESPACE + request.getServletPath();
        return IGNORE_REQUEST_BODY_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(servletPath));
    }
}
