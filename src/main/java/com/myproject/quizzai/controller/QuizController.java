package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.QuizCreationRequestDto;
import com.myproject.quizzai.dto.QuizResponseDto;
import com.myproject.quizzai.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(QuizController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Quiz Controller", description = "Controller for managing quizzes")
public class QuizController {
    public static final String ROOT_MAPPING = "quiz";

    private final QuizService quizService;

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);


    @PostMapping
    @Operation(summary = "Create a new quiz")
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody QuizCreationRequestDto quizCreationRequest) {
        logger.info("create() method called with request: {}", quizCreationRequest);

        String quizId = quizService.create(quizCreationRequest);
        //create a json for response
        Map<String, String> response = new HashMap<>();
        response.put("quizId", quizId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //GET /quiz?id={id}
    @GetMapping
    @Operation(summary = "Get quiz by ID")
    public ResponseEntity<QuizResponseDto> getQuizById(@RequestParam String id) {
        logger.info("getQuizById() method called with ID: {}", id);

        QuizResponseDto quiz = quizService.getQuizById(id);
        if (quiz != null) {
            return ResponseEntity.ok(quiz);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
