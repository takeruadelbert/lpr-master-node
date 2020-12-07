package com.stn.ester.core.filters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.stn.ester.core.configurations.AccessLog.AccessLogQueueSender;
import com.stn.ester.core.filters.wrapper.RequestWrapper;
import com.stn.ester.entities.AccessLog;
import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.helpers.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private Gson gson = new Gson();
    private ObjectMapper mapper = new ObjectMapper();
    private static String WHITESPACE = " ";
    private static final List<String> IGNORE_REQUEST_BODY_URL = Arrays.asList(
            RequestMethod.POST + WHITESPACE + "/users/login",
            RequestMethod.POST + WHITESPACE + "/asset_files/upload",
            RequestMethod.POST + WHITESPACE + "/asset_files/upload-encoded",
            RequestMethod.POST + WHITESPACE + "/asset_files/upload-url"
    );
    private boolean accessLogEnabled;

    @Autowired
    private AccessLogQueueSender accessLogQueueSender;

    @Autowired
    public RequestLoggingFilter(@Value("${ester.logging.access.enabled}") boolean accessLogEnabled) {
        this.accessLogEnabled = accessLogEnabled;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // lazy set AccessLogQueueSender because Spring cannot inject it here.
        if (accessLogQueueSender == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            accessLogQueueSender = webApplicationContext.getBean(AccessLogQueueSender.class);
        }

        // make copy request of Input Stream
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ServletRequest copiedRequest = requestWrapper;

        String IPAddressClient = request.getHeader("X-FORWARDED-FOR");
        String queryParam = request.getQueryString();
        String URI = queryParam == null ? request.getRequestURI() : request.getRequestURI() + "?" + queryParam;
        String request_method = request.getMethod();
        Long user_id = SessionHelper.getUserID();
        if (IPAddressClient == null || "".equals(IPAddressClient)) {
            IPAddressClient = request.getRemoteAddr();
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request_method);
        String requestBody = null;

        if (!this.isExcludedFromList(request)) {
            // get request body
            try {
                mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
                Map jsonMap = mapper.readValue(copiedRequest.getInputStream(), Map.class);
                requestBody = this.gson.toJson(jsonMap);
            } catch (MismatchedInputException | JsonParseException ex) {
                requestBody = null;
            }
        }

        AccessLog accessLog = new AccessLog(IPAddressClient, URI, requestMethod, user_id, requestBody);
        doAccessLog(accessLog);

        chain.doFilter(copiedRequest, response);
    }

    @Async
    public void doAccessLog(AccessLog accessLog) {
        if (accessLogEnabled) {
            accessLogQueueSender.send(accessLog);
        }
    }

    private boolean isExcludedFromList(HttpServletRequest request) {
        String servletPath = RequestMethod.valueOf(request.getMethod()) + WHITESPACE + request.getServletPath();
        return IGNORE_REQUEST_BODY_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(servletPath));
    }
}
