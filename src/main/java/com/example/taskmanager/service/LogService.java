package com.example.taskmanager.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LogService {
    private static final String LOG_FILE_PATH = "/app/logs/app.log";
    private static final long PROCESSING_DELAY_MS = 15000; // 15 seconds delay
    private static final String TEMP_FILE_PREFIX = "logs_";
    private static final String TEMP_FILE_SUFFIX = ".log";

    private final Map<UUID, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<UUID, Path> taskLogFiles = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Async
    public CompletableFuture<UUID> initiateLogCollection(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        UUID taskId = UUID.randomUUID();
        taskStatusMap.put(taskId, new TaskStatus(taskId, Status.IN_PROGRESS,
                "Log collection started for date " + date));

        scheduler.schedule(() -> processLogFile(taskId, date), PROCESSING_DELAY_MS, TimeUnit.MILLISECONDS);

        return CompletableFuture.completedFuture(taskId);
    }

    private void processLogFile(UUID taskId, LocalDate date) {
        try {
            File logFile = new File(LOG_FILE_PATH);
            if (!logFile.exists()) {
                updateTaskStatus(taskId, Status.FAILED, "Log file not found");
                return;
            }

            String dateStr = date.toString();
            List<String> filteredLines = filterLinesByDate(logFile, dateStr);

            if (filteredLines.isEmpty()) {
                updateTaskStatus(taskId, Status.COMPLETED, "No logs for date " + dateStr);
                return;
            }

            Path tempFile = createTempFileWithLogs(dateStr, filteredLines);
            taskLogFiles.put(taskId, tempFile);
            updateTaskStatus(taskId, Status.COMPLETED, "Log collection completed for date " + dateStr);
        } catch (IOException e) {
            updateTaskStatus(taskId, Status.FAILED, "Error processing logs: " + e.getMessage());
        }
    }

    private List<String> filterLinesByDate(File logFile, String dateStr) throws IOException {
        List<String> filteredLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(dateStr)) {
                    filteredLines.add(line);
                }
            }
        }
        return filteredLines;
    }

    private Path createTempFileWithLogs(String dateStr, List<String> filteredLines) throws IOException {
        Path tempFile = Files.createTempFile(TEMP_FILE_PREFIX + dateStr + "_", TEMP_FILE_SUFFIX);
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            for (String line : filteredLines) {
                writer.write(line);
                writer.newLine();
            }
        }
        return tempFile;
    }

    private void updateTaskStatus(UUID taskId, Status status, String message) {
        taskStatusMap.put(taskId, new TaskStatus(taskId, status, message));
    }

    public TaskStatus getTaskStatus(UUID taskId) {
        if (taskId == null) {
            return new TaskStatus(null, Status.NOT_FOUND, "Task ID cannot be null");
        }
        return taskStatusMap.getOrDefault(taskId,
                new TaskStatus(taskId, Status.NOT_FOUND, "Task not found"));
    }

    @Async
    public CompletableFuture<Resource> getLogFileByTaskId(UUID taskId) {
        return CompletableFuture.supplyAsync(() -> {
            validateTaskId(taskId);
            TaskStatus status = getTaskStatus(taskId);
            validateTaskStatus(status);

            Path logFile = taskLogFiles.get(taskId);
            if (logFile == null || !Files.exists(logFile)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log file not found");
            }

            return new FileSystemResource(logFile.toFile());
        });
    }

    private void validateTaskId(UUID taskId) {
        if (taskId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task ID cannot be null");
        }
    }

    private void validateTaskStatus(TaskStatus status) {
        if (status.getStatus() == Status.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        if (status.getStatus() == Status.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log file not ready yet");
        }

        if (status.getStatus() == Status.FAILED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, status.getMessage());
        }

        if (status.getMessage() != null && status.getMessage().startsWith("No logs for")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public enum Status {
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        NOT_FOUND
    }

    @Data
    @AllArgsConstructor
    public static class TaskStatus {
        private UUID taskId;
        private Status status;
        private String message;
    }
}