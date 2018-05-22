package com.gd.orh.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.gd.orh.dto.CarLocationDTO;
import com.gd.orh.dto.TripDTO;
import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.entity.AuthorityName;
import com.gd.orh.security.JwtTokenUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MessageEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEventHandler.class);

    //会话集合
    private static final ConcurrentSkipListMap<String, ClientInfo> WEB_SOCKET_MAP = new ConcurrentSkipListMap<>();

    //静态变量，用来记录当前在线连接数。（原子类、线程安全）
    private static AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    private static final String BROADCAST_CAR_LOCATION_ROOM = "broadcastCarLocation";
    private static final String BROADCAST_TRIP_ROOM = "broadcastTrip";

    @Autowired
    private SocketIOServer server;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * connect事件处理，当客户端发起连接时将调用
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");

        String username = jwtTokenUtil.getUsernameFromToken(token);
        LOGGER.info("User '{}' connected successful!", username);

        UUID sessionId = client.getSessionId();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(username);

        // 如果没有连接信息、则新建会话信息
        if (clientInfo == null) {
            clientInfo = new ClientInfo();
            clientInfo.setUsername(username);
            clientInfo.setOnline(true);

            LOGGER.info("Established session '{}' for user '{}'.", sessionId, username);
            // 在线数加1
            LOGGER.info("Current connection count is {}.", ONLINE_COUNT.incrementAndGet());
        }

        // 更新设置客户端连接信息
        clientInfo.setLeastSignificantBits(sessionId.getLeastSignificantBits());
        clientInfo.setMostSignificantBits(sessionId.getMostSignificantBits());
        clientInfo.setLastConnectedTime(new Date());
        // 将会话信息更新保存至集合中
        WEB_SOCKET_MAP.put(username, clientInfo);

        String role = client.getHandshakeData().getSingleUrlParam("role");
        if (AuthorityName.ROLE_PASSENGER.name().equalsIgnoreCase(role)) {
            client.joinRoom(BROADCAST_CAR_LOCATION_ROOM);
        } else if (AuthorityName.ROLE_DRIVER.name().equalsIgnoreCase(role)) {
            client.joinRoom(BROADCAST_TRIP_ROOM);
        }
    }

    /**
     * disconnect事件处理，当客户端断开连接时将调用
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");

        String username = jwtTokenUtil.getUsernameFromToken(token);
        WEB_SOCKET_MAP.remove(username);

        LOGGER.info("Close session '{}' for user '{}'.", client.getSessionId(), username);
        // 在线数减1
        LOGGER.info("Current connection count is {}.", ONLINE_COUNT.decrementAndGet());

        String role = client.getHandshakeData().getSingleUrlParam("role");
        if (AuthorityName.ROLE_PASSENGER.name().equalsIgnoreCase(role)) {
            client.leaveRoom(BROADCAST_CAR_LOCATION_ROOM);
        } else if (AuthorityName.ROLE_DRIVER.name().equalsIgnoreCase(role)) {
            client.leaveRoom(BROADCAST_TRIP_ROOM);
        }
    }

    // 车主上传车辆位置，广播给乘客
    @OnEvent(value = "broadcastCarLocation")
    public void onBroadcastCarLocation(SocketIOClient client, AckRequest ackRequest, CarLocationDTO carLocationDTO) {
        LOGGER.info("Broadcast car location.");

        server.getRoomOperations(BROADCAST_CAR_LOCATION_ROOM).sendEvent("broadcastCarLocation", carLocationDTO);
    }

    // 乘客发布行程，广播给正在听单的车主
    @OnEvent(value = "publishTrip")
    public void onPublishTrip(SocketIOClient client, AckRequest ackRequest, TripDTO tripDTO) {
        LOGGER.info("Broadcast published trip.");

        client.leaveRoom(BROADCAST_CAR_LOCATION_ROOM);
        server.getRoomOperations(BROADCAST_TRIP_ROOM).sendEvent("publishTrip", tripDTO);
    }

    // 乘客取消行程，广播给正在听单的车主
    @OnEvent(value = "cancelTrip")
    public void onCancelTrip(SocketIOClient client, AckRequest ackRequest, TripDTO tripDTO) {
        LOGGER.info("Broadcast canceled trip.");

        client.joinRoom(BROADCAST_CAR_LOCATION_ROOM);
        server.getRoomOperations(BROADCAST_TRIP_ROOM).sendEvent("cancelTrip", tripDTO);
    }

    // 受理订单，通知目标乘客
    @OnEvent(value = "acceptTrip")
    public void onAcceptTrip(SocketIOClient client, AckRequest request, TripOrderDTO tripOrderDTO) {
        String passengerUsername = tripOrderDTO.getPassengerUsername();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(passengerUsername);

        if (clientInfo != null && clientInfo.isOnline()) {
            UUID target = new UUID(clientInfo.getMostSignificantBits(), clientInfo.getLeastSignificantBits());
            LOGGER.info("User {''} will receive a acceptance notification.", passengerUsername);

            client.leaveRoom(BROADCAST_TRIP_ROOM);

            // client.sendEvent(event, data);
            // 向当前会话发送信息

            // 向目标会话发送信息
            server.getClient(target).sendEvent("acceptTrip", tripOrderDTO);
        }
    }

    // 车主上传车辆位置，单独发给乘客
    @OnEvent(value = "uploadCarLocation")
    public void onUploadCarLocation(SocketIOClient client, AckRequest request, MessageInfo<CarLocationDTO> messageInfo) {
        String passengerUsername = messageInfo.getTargetUsername();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(passengerUsername);

        if (clientInfo != null && clientInfo.isOnline()) {
            UUID target = new UUID(clientInfo.getMostSignificantBits(), clientInfo.getLeastSignificantBits());
            LOGGER.info("User {''} will receive a car location.", passengerUsername);

            // 向目标会话发送信息
            server.getClient(target).sendEvent("receiveCarLocation", messageInfo.getData());
        }
    }

    // 取消订单，通知目标乘客(车主)
    @OnEvent(value = "cancelOrder")
    public void onCancelOrder(SocketIOClient client, AckRequest request, MessageInfo<TripOrderDTO> messageInfo) {
        String targetUsername = messageInfo.getTargetUsername();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(targetUsername);

        if (clientInfo != null && clientInfo.isOnline()) {
            UUID target = new UUID(clientInfo.getMostSignificantBits(), clientInfo.getLeastSignificantBits());
            LOGGER.info("User {''} will receive a cancel notification.", targetUsername);

            String role = client.getHandshakeData().getSingleUrlParam("role");
            SocketIOClient targetClient = server.getClient(target);
            if (AuthorityName.ROLE_PASSENGER.name().equalsIgnoreCase(role)) {
                client.joinRoom(BROADCAST_CAR_LOCATION_ROOM);
                targetClient.joinRoom(BROADCAST_TRIP_ROOM);
            } else if (AuthorityName.ROLE_DRIVER.name().equalsIgnoreCase(role)) {
                client.joinRoom(BROADCAST_TRIP_ROOM);
                targetClient.joinRoom(BROADCAST_CAR_LOCATION_ROOM);
            }

            // 向目标会话发送信息
            targetClient.sendEvent("cancelOrder", messageInfo.getData());
        }
    }

    // 确认乘客上车
    @OnEvent(value = "pickUpPassenger")
    public void onPickUpPassenger(SocketIOClient client, AckRequest request, TripOrderDTO tripOrderDTO) {
        String passengerUsername = tripOrderDTO.getPassengerUsername();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(passengerUsername);

        if (clientInfo != null && clientInfo.isOnline()) {
            UUID target = new UUID(clientInfo.getMostSignificantBits(), clientInfo.getLeastSignificantBits());
            LOGGER.info("User {''} will receive a pick up notification.", passengerUsername);

            // 向目标会话发送信息
            server.getClient(target).sendEvent("pickUpPassenger", tripOrderDTO);
        }
    }

    // 确认乘客到达
    @OnEvent(value = "confirmArrival")
    public void onConfirmArrival(SocketIOClient client, AckRequest request, TripOrderDTO tripOrderDTO) {
        String passengerUsername = tripOrderDTO.getPassengerUsername();

        ClientInfo clientInfo = WEB_SOCKET_MAP.get(passengerUsername);

        if (clientInfo != null && clientInfo.isOnline()) {
            UUID target = new UUID(clientInfo.getMostSignificantBits(), clientInfo.getLeastSignificantBits());
            LOGGER.info("User {''} will receive a arrival notification.", passengerUsername);

            client.joinRoom(BROADCAST_TRIP_ROOM);

            SocketIOClient targetClient = server.getClient(target);
            targetClient.joinRoom(BROADCAST_CAR_LOCATION_ROOM);
            // 向目标会话发送信息
            targetClient.sendEvent("confirmArrival", tripOrderDTO);
        }
    }

    @Data
    public class ClientInfo {
        private String username;
        private boolean isOnline;
        private long mostSignificantBits;
        private long leastSignificantBits;
        private Date lastConnectedTime;
    }

    @Data
    public static class MessageInfo<T> {
        private String targetUsername;

        private T data;
    }
}
