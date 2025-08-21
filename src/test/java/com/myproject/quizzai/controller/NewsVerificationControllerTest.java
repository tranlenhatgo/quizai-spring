package com.myproject.quizzai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.quizzai.dto.NewsVerificationRequestDto;
import com.myproject.quizzai.dto.NewsVerificationResponseDto;
import com.myproject.quizzai.service.NewsVerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsVerificationController.class)
public class NewsVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsVerificationService newsVerificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testVerifyNews() throws Exception {
        // Prepare test data
        NewsVerificationRequestDto request = new NewsVerificationRequestDto();
        request.setQuery("Vắc xin COVID-19 gây vô sinh");
        request.setLanguage("vi");
        request.setMaxSources(8);

        List<NewsVerificationResponseDto.SourceInfo> sources = Arrays.asList(
            NewsVerificationResponseDto.SourceInfo.builder()
                .title("Bộ Y tế thông tin về vắc xin COVID-19")
                .url("https://moh.gov.vn")
                .publisher("Bộ Y tế")
                .supportsQuery(false)
                .credibilityScore(95)
                .build()
        );

        NewsVerificationResponseDto expectedResponse = NewsVerificationResponseDto.builder()
            .query("Vắc xin COVID-19 gây vô sinh")
            .accuracyPercentage(19)
            .confirmedSources(2)
            .totalSources(8)
            .explanation("Test explanation")
            .sources(sources)
            .verdict(NewsVerificationResponseDto.VerificationVerdict.FALSE)
            .build();

        // Mock the service
        when(newsVerificationService.verifyNews(any(NewsVerificationRequestDto.class)))
            .thenReturn(expectedResponse);

        // Perform the test
        mockMvc.perform(post("/news-verification/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value("Vắc xin COVID-19 gây vô sinh"))
                .andExpect(jsonPath("$.accuracyPercentage").value(19))
                .andExpect(jsonPath("$.confirmedSources").value(2))
                .andExpect(jsonPath("$.totalSources").value(8))
                .andExpect(jsonPath("$.verdict").value("FALSE"));
    }

    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/news-verification/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("News verification service is running"));
    }

    @Test
    public void testAutocompleteSuggestions() throws Exception {
        mockMvc.perform(get("/news-verification/autocomplete")
                .param("q", "vắc xin"))
                .andExpect(status().isOk());
    }
}