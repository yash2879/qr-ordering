package in.tablese.tablese_core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DebugController {

    // The new health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(@RequestHeader Map<String, String> headers) {
        System.out.println("Health check endpoint was called.");
        headers.forEach((key, value) -> System.out.println(key + " = " + value));
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/debug/headers")
    public ResponseEntity<Map<String, String>> getHeaders(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> {
            System.out.println("Header: " + key + " = " + value);
        });
        return ResponseEntity.ok(headers);
    }
}