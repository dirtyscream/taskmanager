package com.example.taskmanager.controllers;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {
    private static final String LOG_FILE_PATH = "/app/logs/app.log";

    @GetMapping
    public ResponseEntity<Resource> getLogFileByDate(@RequestParam String date) throws IOException {
        File logFile = new File(LOG_FILE_PATH);
        if (!logFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<String> filteredLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            filteredLines = reader.lines()
                    .filter(line -> line.startsWith(date))
                    .toList();
        }

        if (filteredLines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        File tempFile = Files.createTempFile("logs_" + date + "_" + UUID.randomUUID(), ".log").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (String line : filteredLines) {
                writer.write(line);
                writer.newLine();
            }
        }

        Resource resource = new FileSystemResource(tempFile);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"logs_" + date + ".log\"")
                .body(resource);
    }
}
