package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceWebSocketHandler extends TextWebSocketHandler {

    // deviceId -> session
    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // deviceId -> relay states
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> devices = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("New connection: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();

        // First message from ESP should be: REGISTER:deviceId
        if (payload.startsWith("REGISTER:")) {
            String deviceId = payload.split(":")[1];
            sessions.put(deviceId, session);

            devices.putIfAbsent(deviceId, new ConcurrentHashMap<>());
            for (int i = 1; i <= 3; i++) {
                devices.get(deviceId).putIfAbsent(i, "OFF");
            }

            System.out.println("Device registered: " + deviceId);
        }
    }

    public void updateRelay(String deviceId, int relayId, String state) throws Exception {

        devices.putIfAbsent(deviceId, new ConcurrentHashMap<>());
        devices.get(deviceId).put(relayId, state);

        WebSocketSession session = sessions.get(deviceId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage("RELAY:" + relayId + ":" + state));
        }
    }

    public String getRelayState(String deviceId, int relayId) {
        return devices.getOrDefault(deviceId, new ConcurrentHashMap<>())
                .getOrDefault(relayId, "OFF");
    }
}