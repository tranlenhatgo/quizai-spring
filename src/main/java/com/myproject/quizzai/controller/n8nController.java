package com.myproject.quizzai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.quizzai.dto.MultipartInputStreamFileResource;
import com.myproject.quizzai.dto.QuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(n8nController.ROOT_MAPPING)
public class n8nController {
    public static final String ROOT_MAPPING = "/n8n";
    private static final String N8N_WEBHOOK_URL = "http://localhost:5678/webhook-test";

    private static final Logger logger = LoggerFactory.getLogger(n8nController.class);
    private final RestTemplate restTemplate;

    public n8nController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // POST /n8n/upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("quiz_id") String quizId,
                                         @RequestParam("data") MultipartFile file) throws IOException {
        logger.info("Received file upload request for quiz ID: {}", quizId);
        uploadFileN8n(quizId, file);
        return ResponseEntity.ok("Forwarded to n8n successfully");
    }

    private void uploadFileN8n(String quizId, MultipartFile file) throws IOException {
        String uploadUrl = N8N_WEBHOOK_URL + "/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Wrap file into resource
        MultipartInputStreamFileResource resource =
                new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename(), file.getSize());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("quiz_id", quizId);
        body.add("data", resource); // matches n8n field name

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        new RestTemplate().postForEntity(uploadUrl, request, String.class);
    }

    // POST /n8n/get-question?quiz_id={quizId}
    @PostMapping(value = "/get-question", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizResponse.Output> sendWebhookWithQuizId(@RequestBody Map<String, String> requestBody) {
        String quizId = requestBody.get("quiz_id");
        logger.info("Sending webhook to n8n with quiz ID: {}", quizId);

        String getQuestionUrl = N8N_WEBHOOK_URL + "/get-question";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestPayload = String.format("{\"quiz_id\": \"%s\"}", quizId);
        HttpEntity<String> request = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(getQuestionUrl, request, String.class);
            logger.info("Received response from n8n: {}", response.getBody());

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            QuizResponse quizResponse = objectMapper.readValue(response.getBody(), QuizResponse.class);

            // Extract the output and return it
            return ResponseEntity.status(response.getStatusCode()).body(quizResponse.getOutput());
        } catch (Exception e) {
            logger.error("Error while sending request to n8n: {}", e.getMessage());
            throw new RuntimeException("Failed to send webhook to n8n", e);
        }
    }
}
