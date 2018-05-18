package com.gd.orh.config;

import com.gd.orh.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;

import java.util.Map;

@AllArgsConstructor
public class WebSocketInterceptor extends ChannelInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Auth-Token");

            logger.info("Get 'Auth-Token': {}", token);

            if (!StringUtils.isEmpty(token)) {

                logger.info("'Auth-Token' is noe Empty");

                Map sessionAttributes =
                        SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());

                sessionAttributes.put(
                    CsrfToken.class.getName(),
                    new DefaultCsrfToken("Auth-Token", "Auth-Token", token)
                );

                String username = jwtTokenUtil.getUsernameFromToken(token);

                logger.info("Get 'username': {}", username);

                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    logger.info("Get 'userDetails': {}", userDetails);

                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication
                                = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                                );

                        logger.info("authorized user '{}', setting security context", username);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);
                    }
                }
            }
        }

        return message;
    }
}
