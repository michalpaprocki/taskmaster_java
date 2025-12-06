package com.mike.taskmaster.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {
    
    @GetMapping("/")
    public String home() {
        return """
                <!DOCTYPE html>
                <html lang="en>
                <head>
                    <meta charset="UTF-8">
                    <title>TaskMaster</title>
                </head>
                <body>
                    <h1>Welcome to TaskMaster API</h1>
                    <p>This is the main page.</p>
                </body>
                </html>
                """;
    }
    
}
