package com.myproject.quizzai.controller;

import com.myproject.quizzai.dto.QuizStartRequest;
import com.myproject.quizzai.dto.TakeQuizStartResponseDto;
import com.myproject.quizzai.service.TakeQuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TakeQuizController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Take Quiz Controller", description = "Controller for managing quiz taked by players")
public class TakeQuizController {
    public static final String ROOT_MAPPING = "take-quiz";
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizController.class);

    private final TakeQuizService takeQuizService;

    @PostMapping("/start")
    @Operation(summary = "Start a quiz")
    public ResponseEntity<TakeQuizStartResponseDto> StartQuiz(@RequestBody QuizStartRequest quizStartRequest) {
        logger.info("StartQuiz() method called with quiz ID: {}", quizStartRequest.getQuizId());

        TakeQuizStartResponseDto startResponseDto = takeQuizService.StartQuiz(quizStartRequest.getQuizId(),
                quizStartRequest.getPlayerName());

        return new ResponseEntity<>(startResponseDto, HttpStatus.OK);
    }
}
