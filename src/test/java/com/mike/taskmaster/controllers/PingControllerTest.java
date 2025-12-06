package com.mike.taskmaster.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PingController.class)
public class PingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test

    void testReturnPong() throws Exception {
        mockMvc.perform(get("/ping")).andExpect(status().isOk()).andExpect(jsonPath("$.ping").value("pong"));
    }
}
