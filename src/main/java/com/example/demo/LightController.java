package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class LightController {

    // Replace with your ESP IP
    private final String espIp = "http://192.168.0.11";

    // ===== Turn relay ON =====
    @GetMapping("/relay/{id}/on")
    public String turnOn(@PathVariable int id, @RequestParam String key) {
        if (!key.equals("mySecret123")) return "Unauthorized";
        return callEsp(id, "on");
    }

    // ===== Turn relay OFF =====
    @GetMapping("/relay/{id}/off")
    public String turnOff(@PathVariable int id, @RequestParam String key) {
        if (!key.equals("mySecret123")) return "Unauthorized";
        return callEsp(id, "off");
    }

    // ===== Get relay status =====
    @GetMapping("/relay/{id}/status")
    public String getStatus(@PathVariable int id) {
        try {
            URL url = new URL(espIp + "/relay/" + id + "/status");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            InputStream response = conn.getInputStream();
            String status = new String(response.readAllBytes());
            response.close();
            return status; // "ON" or "OFF"
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // ===== Internal helper to call ESP endpoints =====
    private String callEsp(int id, String action) {
        try {
            URL url = new URL(espIp + "/relay/" + id + "/" + action);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            InputStream response = conn.getInputStream();
            response.close();
            return "Relay " + id + " " + action.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to call relay " + id;
        }
    }
}