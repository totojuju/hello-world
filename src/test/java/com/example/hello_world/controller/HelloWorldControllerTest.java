package com.example.hello_world.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.hello_world.response.HelloWorldResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("hello-worldテスト")
    void test001() throws Exception {
        
        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.post("/hello-world")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Yuta\"}"))
            .andExpect(status().isOk()).andReturn();
        
        HelloWorldResponse response = mapper.readValue(
            result.getResponse().getContentAsString(), HelloWorldResponse.class
        );

        assertEquals("Hello, Yuta!", response.getMessage());
    }
}
