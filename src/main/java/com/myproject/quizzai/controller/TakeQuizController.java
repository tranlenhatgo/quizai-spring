package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.QuestionResponseDto;
import com.myproject.quizzai.dto.QuizStartRequest;
import com.myproject.quizzai.service.QuestionService;
import com.myproject.quizzai.service.TakeQuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping(TakeQuizController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Take Quiz Controller", description = "Controller for managing quiz taked by players")
public class TakeQuizController {
    public static final String ROOT_MAPPING = "take-quiz";
    private final TakeQuizService takeQuizService;
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizController.class);


    @GetMapping("/start")
    @Operation(summary = "Start a quiz")
    public ResponseEntity<List<QuestionResponseDto>> StartQuiz(@RequestBody QuizStartRequest quizStartRequest) {
        logger.info("StartQuiz() method called with quiz ID: {}", quizStartRequest.getQuizId());

        List<QuestionResponseDto> questions = takeQuizService.StartQuiz(quizStartRequest.getQuizId(),
                quizStartRequest.getPlayerName());
        if (questions != null && !questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
