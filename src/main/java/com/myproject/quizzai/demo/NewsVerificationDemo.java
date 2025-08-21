package com.myproject.quizzai.demo;

import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.service.NewsVerificationService;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Demo class to showcase the news verification functionality
 * This can be run independently without the full Spring Boot context
 */
public class NewsVerificationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== News Verification Platform Demo ===\n");
        
        // Create the service with required dependencies
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        NewsVerificationService service = new NewsVerificationService(restTemplate, objectMapper);
        
        // Demo 1: Verify the example from the problem statement
        System.out.println("1. Testing the example: 'Vắc xin 19 gây vô sinh'");
        NewsVerificationRequestDto request1 = new NewsVerificationRequestDto();
        request1.setQuery("Vắc xin 19 gây vô sinh");
        request1.setLanguage("vi");
        request1.setMaxSources(8);
        
        NewsVerificationResponseDto response1 = service.verifyNews(request1);
        printVerificationResult(response1);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Demo 2: Test autocomplete functionality
        System.out.println("2. Testing autocomplete suggestions for 'vắc xin'");
        var suggestions = service.getAutocompleteSuggestions("vắc xin");
        System.out.println("Suggestions:");
        suggestions.getSuggestions().forEach(suggestion -> 
            System.out.println("  - " + suggestion));
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Demo 3: Test another news verification
        System.out.println("3. Testing another claim: '5G gây ung thư'");
        NewsVerificationRequestDto request2 = new NewsVerificationRequestDto();
        request2.setQuery("5G gây ung thư");
        request2.setLanguage("vi");
        request2.setMaxSources(8);
        
        NewsVerificationResponseDto response2 = service.verifyNews(request2);
        printVerificationResult(response2);
        
        System.out.println("\n=== Demo completed successfully! ===");
    }
    
    private static void printVerificationResult(NewsVerificationResponseDto response) {
        System.out.println("Query: " + response.getQuery());
        System.out.println("Accuracy: " + response.getAccuracyPercentage() + "%");
        System.out.println("Confirmed sources: " + response.getConfirmedSources() + "/" + response.getTotalSources());
        System.out.println("Verdict: " + response.getVerdict());
        System.out.println("Explanation: " + response.getExplanation());
        
        if (response.getSources() != null && !response.getSources().isEmpty()) {
            System.out.println("\nSources:");
            response.getSources().forEach(source -> {
                System.out.println("  • " + source.getTitle());
                System.out.println("    Publisher: " + source.getPublisher());
                System.out.println("    URL: " + source.getUrl());
                System.out.println("    Supports claim: " + (source.getSupportsQuery() ? "Yes" : "No"));
                System.out.println("    Credibility: " + source.getCredibilityScore() + "/100");
                System.out.println();
            });
        }
    }
}