package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.FirstSchema;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirstController {
    @GetMapping("/api/project_query")
    public ResponseEntity<FirstSchema> getProjectWithQuery(
            @RequestParam(value = "name", defaultValue = "None") String name,
            @RequestParam(value = "amountTasks", defaultValue = "0") String amountTasks) {
        try {
            int amountTasksInt = Integer.parseInt(amountTasks);
            FirstSchema response = new FirstSchema(name, amountTasksInt);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/project_path/{projectId}")
    public ResponseEntity<Map<String, Integer>> getProjectWithPath(@PathVariable Integer projectId) {
        Map<String, Integer> response = new HashMap<>();
        response.put("projectId", projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
