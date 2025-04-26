package com.myproject.quizzai.service;

import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.controller.utils.IdUtil;
import com.myproject.quizzai.dto.QuizCreationRequestDto;
import com.myproject.quizzai.dto.QuizResponseDto;
import com.myproject.quizzai.model.Quiz;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final Firestore firestore;

    @SneakyThrows
    public String create(@NonNull final QuizCreationRequestDto quizCreationRequest) {
        String quizId = IdUtil.generateId();

        Quiz quiz = Quiz.builder()
                .id(quizId)
                .host_id(quizCreationRequest.getHost_id())
                .title(quizCreationRequest.getTitle())
                .description(quizCreationRequest.getDescription())
                .categories_id(quizCreationRequest.getCategories_id())
                .start_time(quizCreationRequest.getStart_time())
                .end_time(quizCreationRequest.getEnd_time())
                .createdAt(quizCreationRequest.getCreated_at())
                .updatedAt(quizCreationRequest.getUpdated_at())
                .build();

        firestore.collection("quizzes").document(quizId).set(quiz).get();
        return quizId;
    }

    @SneakyThrows
    public QuizResponseDto getQuizById(@NonNull final String id) {
        Quiz quiz = firestore.collection("quizzes").document(id).get().get().toObject(Quiz.class);
        if (quiz != null) {
            return QuizResponseDto.builder().id(quiz.getId())
                    .host_id(quiz.getHost_id())
                    .title(quiz.getTitle())
                    .description(quiz.getDescription())
                    .status(quiz.getStatus())
                    .categories_id(quiz.getCategories_id())
                    .code(quiz.getCode())
                    .start_time(quiz.getStart_time())
                    .end_time(quiz.getEnd_time())
                    .createdAt(quiz.getCreatedAt())
                    .updatedAt(quiz.getUpdatedAt())
                    .build();
        } else {
            return null;
        }
    }

}
