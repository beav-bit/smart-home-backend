package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class LightController {

    private final String apiKey = "mySecret123";
    private final DeviceWebSocketHandler handler;

    public LightController(DeviceWebSocketHandler handler) {
        this.handler = handler;
    }

    @GetMapping("/device/{deviceId}/relay/{relayId}/on")
    public String turnOn(@PathVariable String deviceId,
                         @PathVariable int relayId,
                         @RequestParam String key) throws Exception {

        if (!key.equals(apiKey)) return "Unauthorized";

        handler.updateRelay(deviceId, relayId, "ON");
        return "ON";
    }

    @GetMapping("/device/{deviceId}/relay/{relayId}/off")
    public String turnOff(@PathVariable String deviceId,
                          @PathVariable int relayId,
                          @RequestParam String key) throws Exception {

        if (!key.equals(apiKey)) return "Unauthorized";

        handler.updateRelay(deviceId, relayId, "OFF");
        return "OFF";
    }

    @GetMapping("/device/{deviceId}/relay/{relayId}/status")
    public String getStatus(@PathVariable String deviceId,
                            @PathVariable int relayId) {

        return handler.getRelayState(deviceId, relayId);
    }
}