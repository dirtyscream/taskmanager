package com.example.taskmanager.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {
    @GetMapping("")
    public ResponseEntity<String> getLogByDate(@RequestParam String date) throws IOException {
        String logFilePath = "/app/logs/app.log";
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            return ResponseEntity.status(404).body("Log file not found.");
        }
        StringBuilder logContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            logContent.append(
                    br.lines()
                            .filter(line -> line.contains(date))
                            .collect(Collectors.joining("\n"))
            );
        }
        if (logContent.isEmpty()) {
            return ResponseEntity.status(404).body("No logs found for the specified date.");
        }
        return ResponseEntity.ok(logContent.toString());
    }
}

