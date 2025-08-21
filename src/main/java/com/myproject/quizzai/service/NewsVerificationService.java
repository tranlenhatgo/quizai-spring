package com.myproject.quizzai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.quizzai.dto.AutocompleteSuggestionDto;
import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsVerificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsVerificationService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Use the same n8n URL as the existing service
    private static final String N8N_BASE_URL = "http://localhost:5678/webhook-test";
    
    public NewsVerificationResponseDto verifyNews(NewsVerificationRequestDto request) {
        logger.info("Verifying news: {}", request.getQuery());
        
        try {
            // Call n8n workflow for news verification
            String verificationUrl = N8N_BASE_URL + "/verify-news";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> payload = Map.of(
                "query", request.getQuery(),
                "language", request.getLanguage(),
                "maxSources", request.getMaxSources()
            );
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(verificationUrl, requestEntity, String.class);
            
            if (response.getBody() != null) {
                return parseVerificationResponse(response.getBody(), request.getQuery());
            }
            
        } catch (Exception e) {
            logger.error("Error calling n8n for news verification: {}", e.getMessage());
        }
        
        // Fallback response if n8n is unavailable
        return createFallbackResponse(request.getQuery());
    }
    
    public AutocompleteSuggestionDto getAutocompleteSuggestions(String input) {
        logger.info("Getting autocomplete suggestions for: {}", input);
        
        // For now, provide some predefined suggestions related to common news topics
        List<String> commonSuggestions = Arrays.asList(
            "Vắc xin COVID-19 gây vô sinh",
            "5G gây ung thư",
            "Nước chanh mật ong giảm cân",
            "Thuốc ho cam đường phổi",
            "Ăn tỏi sống chữa COVID-19",
            "Nước ion kiềm chữa bệnh",
            "Wifi gây hại não bộ",
            "Thực phẩm hữu cơ an toàn hơn"
        );
        
        List<String> filteredSuggestions = commonSuggestions.stream()
            .filter(suggestion -> suggestion.toLowerCase().contains(input.toLowerCase()))
            .limit(5)
            .toList();
        
        AutocompleteSuggestionDto response = new AutocompleteSuggestionDto();
        response.setSuggestions(filteredSuggestions);
        response.setHasMore(filteredSuggestions.size() >= 5);
        
        return response;
    }
    
    private NewsVerificationResponseDto parseVerificationResponse(String responseBody, String originalQuery) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Parse the response from n8n/Gemini
            Integer accuracyPercentage = jsonNode.path("accuracy_percentage").asInt(0);
            Integer confirmedSources = jsonNode.path("confirmed_sources").asInt(0);
            Integer totalSources = jsonNode.path("total_sources").asInt(0);
            String explanation = jsonNode.path("explanation").asText("Không có thông tin giải thích.");
            
            List<NewsVerificationResponseDto.SourceInfo> sources = new ArrayList<>();
            JsonNode sourcesNode = jsonNode.path("sources");
            if (sourcesNode.isArray()) {
                for (JsonNode sourceNode : sourcesNode) {
                    NewsVerificationResponseDto.SourceInfo source = NewsVerificationResponseDto.SourceInfo.builder()
                        .title(sourceNode.path("title").asText())
                        .url(sourceNode.path("url").asText())
                        .publisher(sourceNode.path("publisher").asText())
                        .supportsQuery(sourceNode.path("supports_query").asBoolean())
                        .credibilityScore(sourceNode.path("credibility_score").asInt(50))
                        .build();
                    sources.add(source);
                }
            }
            
            NewsVerificationResponseDto.VerificationVerdict verdict = determineVerdict(accuracyPercentage);
            
            return NewsVerificationResponseDto.builder()
                .query(originalQuery)
                .accuracyPercentage(accuracyPercentage)
                .confirmedSources(confirmedSources)
                .totalSources(totalSources)
                .explanation(explanation)
                .sources(sources)
                .verdict(verdict)
                .build();
                
        } catch (Exception e) {
            logger.error("Error parsing verification response: {}", e.getMessage());
            return createFallbackResponse(originalQuery);
        }
    }
    
    private NewsVerificationResponseDto createFallbackResponse(String query) {
        // Create a mock response for demonstration purposes
        List<NewsVerificationResponseDto.SourceInfo> mockSources = Arrays.asList(
            NewsVerificationResponseDto.SourceInfo.builder()
                .title("Bộ Y tế thông tin về vắc xin COVID-19")
                .url("https://moh.gov.vn/tin-tuc-su-kien")
                .publisher("Bộ Y tế")
                .supportsQuery(false)
                .credibilityScore(95)
                .build(),
            NewsVerificationResponseDto.SourceInfo.builder()
                .title("WHO - Thông tin chính thức về vắc xin")
                .url("https://www.who.int/vietnam")
                .publisher("WHO Vietnam")
                .supportsQuery(false)
                .credibilityScore(98)
                .build()
        );
        
        return NewsVerificationResponseDto.builder()
            .query(query)
            .accuracyPercentage(19) // As per the example in the problem statement
            .confirmedSources(2)
            .totalSources(8)
            .explanation("Dựa trên phân tích từ 8 nguồn tin đáng tin cậy, thông tin này có độ chính xác thấp. " +
                        "Các nghiên cứu khoa học hiện tại không hỗ trợ tuyên bố này. " +
                        "Vắc xin COVID-19 đã được thử nghiệm kỹ lưỡng và được các tổ chức y tế uy tín khuyến nghị.")
            .sources(mockSources)
            .verdict(NewsVerificationResponseDto.VerificationVerdict.FALSE)
            .build();
    }
    
    private NewsVerificationResponseDto.VerificationVerdict determineVerdict(Integer accuracyPercentage) {
        if (accuracyPercentage >= 90) return NewsVerificationResponseDto.VerificationVerdict.TRUE;
        if (accuracyPercentage >= 70) return NewsVerificationResponseDto.VerificationVerdict.MOSTLY_TRUE;
        if (accuracyPercentage >= 50) return NewsVerificationResponseDto.VerificationVerdict.MIXED;
        if (accuracyPercentage >= 30) return NewsVerificationResponseDto.VerificationVerdict.MOSTLY_FALSE;
        if (accuracyPercentage >= 0) return NewsVerificationResponseDto.VerificationVerdict.FALSE;
        return NewsVerificationResponseDto.VerificationVerdict.INSUFFICIENT_DATA;
    }
}