package com.example.hello_world.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.hello_world.request.HelloWorldRequest;
import com.example.hello_world.response.HelloWorldResponse;

@RestController
public class HelloWorldController {
    
    @PostMapping("/hello-world")
    public ResponseEntity<HelloWorldResponse> helloWorld(@RequestBody HelloWorldRequest request) {
        HelloWorldResponse response = new HelloWorldResponse();
        response.setMessage("Hello, " + request.getName() + "!");
        return ResponseEntity.ok(response);
    }
}
