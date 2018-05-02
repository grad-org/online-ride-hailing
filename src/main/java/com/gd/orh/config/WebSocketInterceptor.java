package com.gd.orh.config;

import com.gd.orh.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class WebSocketInterceptor extends ChannelInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            logger.info("command is CONNECT");

            String token = accessor.getFirstNativeHeader("Auth-Token");
            String username = null;

            if (!StringUtils.isEmpty(token)) {
                logger.info("token is not empty");

                Map sessionAttributes =
                        SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());

                sessionAttributes.put(
                    CsrfToken.class.getName(),
                    new DefaultCsrfToken(
                            "Auth-Token",
                            "Auth-Token",
                            token
                    )
                );

                username = jwtTokenUtil.getUsernameFromToken(token);

                if (username != null) {

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                    if (authentication == null) {
                        logger.info("security context was null, so authorizing user");

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            logger.info("authorized user '{}', setting security context", username);

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }

                    accessor.setUser(authentication);
                }
            }
        }

        return message;
    }
}
