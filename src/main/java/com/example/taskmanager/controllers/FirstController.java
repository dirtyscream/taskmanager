package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.FirstSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
    @GetMapping("/api/project_query")
    public ResponseEntity<FirstSchema> getProjectWithQuery(
            @RequestParam(value = "name", defaultValue = "None") String name,
            @RequestParam(value = "amountTasks", defaultValue = "0") String amountTasks) {
        return getResponseFromUrl(name, amountTasks);
    }

    @GetMapping("/api/project_path/{name}/{amountTasks}")
    public ResponseEntity<FirstSchema> getProjectWithPath(
            @PathVariable("name") String name,
            @PathVariable String amountTasks) {
        return getResponseFromUrl(name, amountTasks);
    }

    private ResponseEntity<FirstSchema> getResponseFromUrl(@PathVariable("name") String name,
                                                           @PathVariable String amountTasks) {
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
}
