package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.service.MockNewsVerificationService;
import com.myproject.quizzai.service.NewsVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(NewsVerificationController.ROOT_MAPPING)
@Tag(name = "News Verification Controller", description = "Controller for verifying news authenticity and accuracy")
public class NewsVerificationController {
    
    public static final String ROOT_MAPPING = "news-verification";
    private static final Logger logger = LoggerFactory.getLogger(NewsVerificationController.class);

    private final NewsVerificationService newsVerificationService;
    private final MockNewsVerificationService mockNewsVerificationService;

    public NewsVerificationController(
            @Autowired(required = false) NewsVerificationService newsVerificationService,
            @Autowired(required = false) MockNewsVerificationService mockNewsVerificationService) {
        this.newsVerificationService = newsVerificationService;
        this.mockNewsVerificationService = mockNewsVerificationService;
    }

    private Object getActiveService() {
        return mockNewsVerificationService != null ? mockNewsVerificationService : newsVerificationService;
    }

    @PostMapping
    @Operation(summary = "Submit news content for verification")
    public ResponseEntity<Map<String, String>> submitForVerification(@RequestBody NewsVerificationRequestDto request) {
        logger.info("submitForVerification() method called with content: {}", request.getContent());

        try {
            String verificationId;
            if (mockNewsVerificationService != null) {
                verificationId = mockNewsVerificationService.createVerification(request);
            } else {
                verificationId = newsVerificationService.createVerification(request);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("verificationId", verificationId);
            response.put("status", "SUBMITTED");
            response.put("message", "Tin tức đã được gửi để xác minh. Vui lòng chờ kết quả.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error submitting news for verification", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra khi gửi tin tức để xác minh");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{verificationId}")
    @Operation(summary = "Get verification result by ID")
    public ResponseEntity<NewsVerificationResponseDto> getVerificationResult(@PathVariable String verificationId) {
        logger.info("getVerificationResult() method called with ID: {}", verificationId);

        NewsVerificationResponseDto result;
        if (mockNewsVerificationService != null) {
            result = mockNewsVerificationService.getVerificationResult(verificationId);
        } else {
            result = newsVerificationService.getVerificationResult(verificationId);
        }
        
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{verificationId}/process")
    @Operation(summary = "Process verification and get AI analysis")
    public ResponseEntity<NewsVerificationResponseDto> processVerification(@PathVariable String verificationId) {
        logger.info("processVerification() method called with ID: {}", verificationId);

        try {
            NewsVerificationResponseDto result;
            if (mockNewsVerificationService != null) {
                result = mockNewsVerificationService.processVerification(verificationId);
            } else {
                result = newsVerificationService.processVerification(verificationId);
            }
            
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error processing verification for ID: {}", verificationId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "Get autocomplete suggestions for news content")
    public ResponseEntity<List<String>> getAutocompleteSuggestions(@RequestParam String query) {
        logger.info("getAutocompleteSuggestions() method called with query: {}", query);

        try {
            List<String> suggestions;
            if (mockNewsVerificationService != null) {
                suggestions = mockNewsVerificationService.getAutocompleteSuggestions(query);
            } else {
                suggestions = newsVerificationService.getAutocompleteSuggestions(query);
            }
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            logger.error("Error getting autocomplete suggestions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Submit and immediately process news verification")
    public ResponseEntity<NewsVerificationResponseDto> verifyNews(@RequestBody NewsVerificationRequestDto request) {
        logger.info("verifyNews() method called with content: {}", request.getContent());

        try {
            if (mockNewsVerificationService != null) {
                // For mock service, create and return immediately
                String verificationId = mockNewsVerificationService.createVerification(request);
                NewsVerificationResponseDto result = mockNewsVerificationService.getVerificationResult(verificationId);
                return ResponseEntity.ok(result);
            } else {
                // Create verification record
                String verificationId = newsVerificationService.createVerification(request);
                
                // Immediately process it
                NewsVerificationResponseDto result = newsVerificationService.processVerification(verificationId);
                
                if (result != null) {
                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (Exception e) {
            logger.error("Error verifying news", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}