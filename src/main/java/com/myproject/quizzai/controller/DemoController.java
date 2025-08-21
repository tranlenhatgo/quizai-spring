package com.myproject.quizzai.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class DemoController {

    @GetMapping("/")
    @ResponseBody
    public String demo() throws IOException {
        // Read the demo.html file and return it
        String content = new String(Files.readAllBytes(Paths.get("demo.html")));
        return content;
    }

    @GetMapping(value = "/demo", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String demoPage() throws IOException {
        // Read the demo.html file and return it
        String content = new String(Files.readAllBytes(Paths.get("demo.html")));
        return content;
    }
}