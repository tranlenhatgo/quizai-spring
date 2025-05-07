package com.myproject.quizzai.service;

import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.QuestionResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeQuizService {

    private Firestore firestore;
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    private QuestionService questionService;

    @SneakyThrows
    public List<QuestionResponseDto> StartQuiz(String quizId, String playerName) {
        List<QuestionResponseDto> questionDtos = questionService.getQuestionsByQuizId(quizId);
        //chua xong
        return questionDtos;
    }

}
