package com.myproject.quizzai.service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.model.NewsVerification;
import com.myproject.quizzai.model.Status;
import com.myproject.quizzai.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsVerificationService {
    
    private final Firestore firestore;
    private final n8nService n8nService;
    
    private static final Logger logger = LoggerFactory.getLogger(NewsVerificationService.class);

    @SneakyThrows
    public String createVerification(NewsVerificationRequestDto request) {
        logger.info("Creating news verification for content: {}", request.getContent());
        
        String verificationId = IdUtil.generateId();
        
        // Create initial verification record
        NewsVerification verification = NewsVerification.builder()
                .id(verificationId)
                .content(request.getContent())
                .status(Status.PENDING)
                .createdAt(Timestamp.now())
                .updatedAt(Timestamp.now())
                .build();
        
        // Save to Firestore
        firestore.collection("news_verification").document(verificationId).set(verification).get();
        
        logger.info("News verification created with ID: {}", verificationId);
        return verificationId;
    }

    @SneakyThrows
    public NewsVerificationResponseDto getVerificationResult(String verificationId) {
        logger.info("Getting verification result for ID: {}", verificationId);
        
        var document = firestore.collection("news_verification").document(verificationId).get().get();
        
        if (!document.exists()) {
            logger.warn("Verification not found for ID: {}", verificationId);
            return null;
        }
        
        NewsVerification verification = document.toObject(NewsVerification.class);
        
        return NewsVerificationResponseDto.builder()
                .id(verification.getId())
                .content(verification.getContent())
                .accuracyPercentage(verification.getAccuracyPercentage())
                .confirmedSources(verification.getConfirmedSources())
                .totalSources(verification.getTotalSources())
                .sources(verification.getSources())
                .status(verification.getStatus().toString())
                .summary(buildSummary(verification))
                .build();
    }

    @SneakyThrows
    public NewsVerificationResponseDto processVerification(String verificationId) {
        logger.info("Processing verification for ID: {}", verificationId);
        
        // Get the verification record
        var document = firestore.collection("news_verification").document(verificationId).get().get();
        
        if (!document.exists()) {
            logger.warn("Verification not found for ID: {}", verificationId);
            return null;
        }
        
        NewsVerification verification = document.toObject(NewsVerification.class);
        
        try {
            // Call n8n service to process the verification
            var n8nResponse = n8nService.getNewsVerificationFromN8n(verification.getContent());
            
            if (n8nResponse != null && n8nResponse.getBody() != null) {
                var result = n8nResponse.getBody();
                
                // Update verification with results
                verification.setAccuracyPercentage(result.getAccuracy());
                verification.setConfirmedSources(result.getConfirmedSources());
                verification.setTotalSources(result.getTotalSources());
                verification.setSources(result.getSources());
                verification.setStatus(Status.COMPLETED);
                verification.setUpdatedAt(Timestamp.now());
                
                // Save updated verification
                firestore.collection("news_verification").document(verificationId).set(verification).get();
                
                logger.info("Verification completed for ID: {}", verificationId);
                
                return NewsVerificationResponseDto.builder()
                        .id(verification.getId())
                        .content(verification.getContent())
                        .accuracyPercentage(verification.getAccuracyPercentage())
                        .confirmedSources(verification.getConfirmedSources())
                        .totalSources(verification.getTotalSources())
                        .sources(verification.getSources())
                        .status(verification.getStatus().toString())
                        .summary(buildSummary(verification))
                        .build();
            } else {
                logger.error("No response from n8n for verification ID: {}", verificationId);
                updateVerificationStatus(verificationId, Status.FAILED);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Error processing verification for ID: {}", verificationId, e);
            updateVerificationStatus(verificationId, Status.FAILED);
            throw e;
        }
    }

    @SneakyThrows
    public List<String> getAutocompleteSuggestions(String query) {
        logger.info("Getting autocomplete suggestions for query: {}", query);
        
        // For now, return some predefined suggestions based on common news topics
        // In a real implementation, this could query a database of recent news topics
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

    @SneakyThrows
    private void updateVerificationStatus(String verificationId, Status status) {
        var document = firestore.collection("news_verification").document(verificationId).get().get();
        if (document.exists()) {
            NewsVerification verification = document.toObject(NewsVerification.class);
            verification.setStatus(status);
            verification.setUpdatedAt(Timestamp.now());
            firestore.collection("news_verification").document(verificationId).set(verification).get();
        }
    }

    private String buildSummary(NewsVerification verification) {
        if (verification.getAccuracyPercentage() == null || verification.getConfirmedSources() == null || verification.getTotalSources() == null) {
            return "Đang xử lý...";
        }
        
        return String.format("Độ chính xác %.0f%%, có %d/%d nguồn xác nhận chính xác", 
                verification.getAccuracyPercentage(), 
                verification.getConfirmedSources(), 
                verification.getTotalSources());
    }
}