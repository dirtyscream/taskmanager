package com.example.taskmanager.controllers;

import com.example.taskmanager.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @Operation(summary = "Инициировать создание лог файла",
            description = "Запускает процесс сбора логов за указанную дату и возвращает ID задачи.")
    @PostMapping("/{date}")
    public CompletableFuture<ResponseEntity<String>> initiateLogCollection(
            @Parameter(description = "Дата лога в формате YYYY-MM-DD", example = "2023-10-27")
            @PathVariable String date) {
        try {
            LocalDate logDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return logService.initiateLogCollection(logDate)
                    .thenApply(taskId -> ResponseEntity.status(HttpStatus.ACCEPTED).body(taskId.toString()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format", e);
        }
    }

    @Operation(summary = "Получить статус задачи",
            description = "Возвращает статус задачи по сбору логов по ID.")
    @GetMapping("/status/{taskId}")
    public ResponseEntity<LogService.TaskStatus> getTaskStatus(
            @Parameter(description = "ID задачи",
                    example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable UUID taskId) {
        LogService.TaskStatus status = logService.getTaskStatus(taskId);
        if (status.getStatus() == LogService.Status.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Получить лог файл по дате",
            description = "Возвращает лог файл за указанную дату в виде файла для скачивания.")
    @GetMapping("/download-by-date")
    public CompletableFuture<ResponseEntity<Resource>> getLogFileByDateAsync(
            @Parameter(description = "Дата лога в формате YYYY-MM-DD", example = "2023-10-27")
            @RequestParam String date) {
        try {
            LocalDate logDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return logService.getLogFileByDateAsync(logDate)
                    .thenApply(resource -> {
                        if (resource == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                        return ResponseEntity.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        "attachment; filename=\"logs_" + date + ".log\"")
                                .body(resource);
                    });
        } catch (DateTimeParseException e) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body(null));
        }
    }
}