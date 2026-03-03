package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LightController {

    private final String apiKey = "mySecret123";

    // Store relay states in memory
    private ConcurrentHashMap<Integer, String> relayStates = new ConcurrentHashMap<>();

    public LightController() {
        relayStates.put(1, "OFF");
        relayStates.put(2, "OFF");
        relayStates.put(3, "OFF");
    }

    @GetMapping("/relay/{id}/on")
    public String turnOn(@PathVariable int id, @RequestParam String key) {
        if (!key.equals(apiKey)) return "Unauthorized";
        relayStates.put(id, "ON");
        return "Relay " + id + " ON";
    }

    @GetMapping("/relay/{id}/off")
    public String turnOff(@PathVariable int id, @RequestParam String key) {
        if (!key.equals(apiKey)) return "Unauthorized";
        relayStates.put(id, "OFF");
        return "Relay " + id + " OFF";
    }

    @GetMapping("/relay/{id}/status")
    public String getStatus(@PathVariable int id) {
        return relayStates.getOrDefault(id, "OFF");
    }
}