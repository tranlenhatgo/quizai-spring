package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.AutocompleteSuggestionDto;
import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.service.NewsVerificationService;
import com.myproject.quizzai.utils.RestVerifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(NewsVerificationController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "News Verification Controller", description = "Controller for verifying news and detecting fake information")
public class NewsVerificationController {
    
    public static final String ROOT_MAPPING = "/news-verification";
    private static final Logger logger = LoggerFactory.getLogger(NewsVerificationController.class);
    
    private final NewsVerificationService newsVerificationService;
    
    @PostMapping("/verify")
    @Operation(summary = "Verify news accuracy and detect fake information")
    public ResponseEntity<NewsVerificationResponseDto> verifyNews(
            @Valid @RequestBody NewsVerificationRequestDto request,
            BindingResult result) {
        
        logger.info("Received news verification request: {}", request.getQuery());
        
        // Validate request
        RestVerifier.verifyModelResult(result);
        
        NewsVerificationResponseDto response = newsVerificationService.verifyNews(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/autocomplete")
    @Operation(summary = "Get autocomplete suggestions for news queries")
    public ResponseEntity<AutocompleteSuggestionDto> getAutocompleteSuggestions(
            @RequestParam("q") String query) {
        
        logger.info("Received autocomplete request for: {}", query);
        
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        AutocompleteSuggestionDto suggestions = newsVerificationService.getAutocompleteSuggestions(query.trim());
        
        return ResponseEntity.ok(suggestions);
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check for news verification service")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("News verification service is running");
    }
}