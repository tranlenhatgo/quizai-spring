package com.myproject.quizzai.service;

import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.model.SourceInfo;
import com.myproject.quizzai.model.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.use-mock-services", havingValue = "true")
public class MockNewsVerificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(MockNewsVerificationService.class);
    private final ConcurrentHashMap<String, NewsVerificationResponseDto> mockData = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public String createVerification(NewsVerificationRequestDto request) {
        logger.info("Mock: Creating news verification for content: {}", request.getContent());
        
        String verificationId = "mock-" + idCounter.getAndIncrement();
        
        // Create mock response with realistic data
        List<SourceInfo> sources = createMockSources(request.getContent());
        double accuracy = calculateMockAccuracy(request.getContent());
        int confirmedSources = (int) (sources.size() * (accuracy / 100.0));
        
        NewsVerificationResponseDto response = NewsVerificationResponseDto.builder()
                .id(verificationId)
                .content(request.getContent())
                .accuracyPercentage(accuracy)
                .confirmedSources(confirmedSources)
                .totalSources(sources.size())
                .sources(sources)
                .status(Status.COMPLETED.toString())
                .summary(String.format("Độ chính xác %.0f%%, có %d/%d nguồn xác nhận chính xác", 
                        accuracy, confirmedSources, sources.size()))
                .build();
        
        mockData.put(verificationId, response);
        logger.info("Mock: News verification created with ID: {}", verificationId);
        return verificationId;
    }

    public NewsVerificationResponseDto getVerificationResult(String verificationId) {
        logger.info("Mock: Getting verification result for ID: {}", verificationId);
        return mockData.get(verificationId);
    }

    public NewsVerificationResponseDto processVerification(String verificationId) {
        logger.info("Mock: Processing verification for ID: {}", verificationId);
        return mockData.get(verificationId);
    }

    public List<String> getAutocompleteSuggestions(String query) {
        logger.info("Mock: Getting autocomplete suggestions for query: {}", query);
        
        List<String> suggestions = new ArrayList<>();
        
        if (query.toLowerCase().contains("vắc")) {
            suggestions.add("Vắc xin COVID-19 gây vô sinh");
            suggestions.add("Vắc xin có tác dụng phụ");
            suggestions.add("Vắc xin được phê duyệt tại Việt Nam");
        }
        
        if (query.toLowerCase().contains("covid") || query.toLowerCase().contains("corona")) {
            suggestions.add("COVID-19 xuất phát từ phòng thí nghiệm");
            suggestions.add("COVID-19 ảnh hưởng đến trẻ em");
            suggestions.add("COVID-19 có thể điều trị bằng thuốc gia đình");
        }
        
        if (query.toLowerCase().contains("kinh tế")) {
            suggestions.add("Kinh tế Việt Nam tăng trưởng mạnh");
            suggestions.add("Lạm phát ảnh hưởng đến người dân");
            suggestions.add("Giá xăng dầu tăng cao");
        }
        
        return suggestions;
    }

    private List<SourceInfo> createMockSources(String content) {
        List<SourceInfo> sources = new ArrayList<>();
        
        sources.add(SourceInfo.builder()
                .title("Báo cáo chính thức từ Bộ Y tế")
                .url("https://moh.gov.vn/tin-tuc")
                .source("Bộ Y tế")
                .isReliable(true)
                .confirmsClaim(content.toLowerCase().contains("chính thức"))
                .summary("Thông tin được xác nhận bởi cơ quan chính thức")
                .build());
        
        sources.add(SourceInfo.builder()
                .title("Nghiên cứu khoa học quốc tế")
                .url("https://pubmed.ncbi.nlm.nih.gov")
                .source("PubMed")
                .isReliable(true)
                .confirmsClaim(content.toLowerCase().contains("vắc xin") ? false : true)
                .summary("Dữ liệu từ nghiên cứu khoa học")
                .build());
        
        sources.add(SourceInfo.builder()
                .title("Báo cáo từ WHO")
                .url("https://who.int")
                .source("WHO")
                .isReliable(true)
                .confirmsClaim(true)
                .summary("Thông tin từ Tổ chức Y tế Thế giới")
                .build());
        
        sources.add(SourceInfo.builder()
                .title("Tin tức trên mạng xã hội")
                .url("https://facebook.com/post")
                .source("Facebook")
                .isReliable(false)
                .confirmsClaim(false)
                .summary("Thông tin chưa được xác minh từ mạng xã hội")
                .build());
        
        return sources;
    }

    private double calculateMockAccuracy(String content) {
        // Mock accuracy calculation based on content keywords
        if (content.toLowerCase().contains("vô sinh")) {
            return 19.0; // Low accuracy for misinformation
        }
        if (content.toLowerCase().contains("chính thức")) {
            return 85.0; // High accuracy for official info
        }
        if (content.toLowerCase().contains("covid")) {
            return 75.0; // Medium-high accuracy for COVID info
        }
        return 50.0; // Default medium accuracy
    }
}