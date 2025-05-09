package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.TakeQuestionSaveRequestDto;
import com.myproject.quizzai.model.TakeQuestion;
import com.myproject.quizzai.service.TakeQuestionService;
import com.myproject.quizzai.service.TakeQuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(TakeQuestionController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Take Question Controller", description = "Controller for managing questions taked by players")
public class TakeQuestionController {
    public static final String ROOT_MAPPING = "take-question";
    private static final Logger logger = LoggerFactory.getLogger(TakeQuestionController.class);

    private final TakeQuestionService takeQuestionService;

    @PostMapping("/save")
    @Operation(summary = "Save list of questions taken by players")
    public ResponseEntity<Map<String, String>> saveTakeQuestions(@RequestBody List<TakeQuestionSaveRequestDto> takeQuestionDtos) {
        logger.info("saveTakeQuestions() method called with questions: {}", takeQuestionDtos);

        Map<String, String> result = takeQuestionService.saveTakeQuestions(takeQuestionDtos);
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
