package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.QuestionCreationRequestDto;
import com.myproject.quizzai.model.Question;
import com.myproject.quizzai.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(QuestionController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Question Controller", description = "Controller for managing questions")
public class QuestionController {
    public static final String ROOT_MAPPING = "question";

    private final QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    //POST /question
    @PostMapping
    @Operation(summary = "Create a list of questions for a quiz")
    public ResponseEntity<Map<String,String>> createQuestions(
            @RequestBody List<QuestionCreationRequestDto> questionDtos) {
        logger.info("createQuestions() method called");

        Map<String,String> result = questionService.createQuestions(questionDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //GET /question?quizId={quizId}
    @GetMapping
    @Operation(summary = "Get list question by quiz ID")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@RequestParam String quizId) {
        logger.info("getQuestionsByQuizId() method called with quiz ID: {}", quizId);

        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
        if (questions != null && !questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //GET /question/{id}
    @GetMapping
    @Operation(summary = "Get question by ID")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
        logger.info("getQuestionById() method called with ID: {}", id);

        Question question = questionService.getQuestionById(id);
        if (question != null) {
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
