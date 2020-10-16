package com.stn.ester.core.interceptors;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.stn.ester.core.events.HeartbeatEvent;
import com.stn.ester.core.exceptions.MultipleLoginException;
import com.stn.ester.services.AuthenticationService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.stn.ester.constants.AuthMessage.*;
import static com.stn.ester.core.security.SecurityConstants.AUTHORIZATION_HEADER_STRING;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {


    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;

    private final AuthenticationService authenticationService;

    private ApplicationEventPublisher applicationEventPublisher;
    private UserService userService;

    @Autowired
    public WebSocketAuthInterceptor(final AuthenticationService authenticationService,
                                    ApplicationEventPublisher applicationEventPublisher,
                                    UserService userService) {
        this.authenticationService = authenticationService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String jwtToken = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER_STRING);
            usernamePasswordAuthenticationToken = checkToken(jwtToken, accessor.getSessionId());
            if (usernamePasswordAuthenticationToken != null) {
                accessor.setUser(usernamePasswordAuthenticationToken);
            }
        } else {
            usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) accessor.getUser();
            if (usernamePasswordAuthenticationToken != null && usernamePasswordAuthenticationToken.getCredentials() != null) {
                Map<String, Object> credentials = ((Map) usernamePasswordAuthenticationToken.getCredentials());
                if (credentials.containsKey("jwtToken")) {
                    checkToken(credentials.get("jwtToken").toString(), accessor.getSessionId());
                    applicationEventPublisher.publishEvent(new HeartbeatEvent(this, Long.parseLong(((Map) usernamePasswordAuthenticationToken.getCredentials()).get("userId").toString()), accessor.getSessionAttributes().get("remoteAddress").toString()));
                }
            }
        }
        return message;
    }

    private UsernamePasswordAuthenticationToken checkToken(String jwtToken, String sessionId) {
        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setSessionId(sessionId);
        if (jwtToken == null) {
            errorAccessor.setMessage("FORBIDDEN");
            errorAccessor.addNativeHeader("code", "403");
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders()));
            return null;
        }
        try {
            return authenticationService.getAuthentication(jwtToken);
        } catch (MultipleLoginException mle) {
            errorAccessor.setMessage(MULTIPLE_LOGIN_STRING);
            errorAccessor.addNativeHeader("code", "440");
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders()));
        } catch (TokenExpiredException ex) {
            errorAccessor.setMessage(TOKEN_EXPIRED_STRING);
            errorAccessor.addNativeHeader("code", "401");
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders()));
        } catch (DisabledException e) {
            errorAccessor.setMessage(ACCOUNT_DISABLED_STRING);
            errorAccessor.addNativeHeader("code", "401");
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders()));
        } catch (LockedException e) {
            errorAccessor.setMessage(ACCOUNT_BANNED_STRING);
            errorAccessor.addNativeHeader("code", "401");
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders()));
        }
        return null;
    }
}
