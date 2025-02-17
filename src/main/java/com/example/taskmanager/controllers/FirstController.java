package com.example.taskmanager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class FirstController {

    @GetMapping("/api/project_query")
    public ResponseEntity<String> getProjectWithQuery(
            @RequestParam(value = "name", defaultValue = "None") String name,
            @RequestParam(value = "amountTasks", defaultValue = "0") String amountTasks) {
        try {
            int amountTasksInt = Integer.parseInt(amountTasks);
            String responseMessage = "The name of the project is " + name + ". The amount of tasks is " + amountTasksInt;
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid amountTasks parameter, must be a valid integer.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/project_path/{name}/{amountTasks}")
    public ResponseEntity<String> getProjectWithPath(@PathVariable String name, @PathVariable String amountTasks) {
        try {
            int amountTasksInt = Integer.parseInt(amountTasks);
            String responseMessage = "The name of the project is " + name + ". The amount of tasks is " + amountTasksInt;
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid amountTasks parameter, must be a valid integer.", HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
