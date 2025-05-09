package com.myproject.quizzai.service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.QuestionResponseDto;
import com.myproject.quizzai.dto.TakeQuizStartResponseDto;
import com.myproject.quizzai.model.Status;
import com.myproject.quizzai.model.TakeQuiz;
import com.myproject.quizzai.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TakeQuizService {

    private final Firestore firestore;
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    private final QuestionService questionService;

    @SneakyThrows
    public TakeQuizStartResponseDto StartQuiz(String quizId, String playerName) {

        List<QuestionResponseDto> questionDtos = questionService.getQuestionsByQuizId(quizId);

        String id = IdUtil.generateId();

        TakeQuiz takeQuiz = TakeQuiz.builder()
                .id(id)
                .quiz_id(quizId)
                .player_name(playerName)
                .status(Status.PENDING)
                .start_time(Timestamp.now())
                .created_at(Timestamp.now())
                .updated_at(Timestamp.now())
                .build();

        firestore.collection("take_quiz").document(id).set(takeQuiz).get();

        return new TakeQuizStartResponseDto(id,questionDtos);
    }


}
