package com.gd.orh.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gd.orh.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

@Configuration
public class NettySocketConfig {

    @Value("${wss.server.port}")
    private int WSS_PORT;

    @Value("${wss.server.host}")
    private String WSS_HOST;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config
                = new com.corundumstudio.socketio.Configuration();

        //不设置主机、默认绑定0.0.0.0 or ::0
        //config.setHostname(WSS_HOST);
        config.setPort(WSS_PORT);

        config.setJsonSupport(new JacksonJsonSupport() {
            @Override
            protected void init(ObjectMapper objectMapper) {
                super.init(objectMapper);
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            }
        });

        //该处进行身份验证
        config.setAuthorizationListener(handshakeData -> {
            // http://localhost:8081?toekn=...
            String token = handshakeData.getSingleUrlParam("token");
            if (StringUtils.isEmpty(token)) return false;

            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (StringUtils.isEmpty(username)) return false;

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.validateToken(token, userDetails);
        });

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
