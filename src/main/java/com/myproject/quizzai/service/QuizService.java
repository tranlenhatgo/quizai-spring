package com.myproject.quizzai.service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.QuizResponseDto;
import com.myproject.quizzai.model.Category;
import com.myproject.quizzai.model.Status;
import com.myproject.quizzai.utils.IdUtil;
import com.myproject.quizzai.dto.QuizCreationRequestDto;
import com.myproject.quizzai.model.Quiz;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final Firestore firestore;

    @SneakyThrows
    public String create(@NonNull final QuizCreationRequestDto quizCreationRequest) {
        String quizId = IdUtil.generateId();

        //if categories are null, set it to empty list

        Quiz quiz = Quiz.builder()
                .id(quizId)
                .host_id(quizCreationRequest.getUser_id())
                .title(quizCreationRequest.getTitle())
                .description(quizCreationRequest.getDescription())
                .categories(quizCreationRequest.getCategories())
                .status(Status.ACTIVE)
                .start_time(quizCreationRequest.getStart_time())
                .end_time(quizCreationRequest.getEnd_time())
                .createdAt(Timestamp.now())
                .updatedAt(Timestamp.now())
                .build();

        firestore.collection("quiz").document(quizId).set(quiz).get();
        return quizId;
    }

    // Find quiz by User ID
    @SneakyThrows
    public List<QuizResponseDto> getQuizByUserId(@NonNull final String userId) {

        List<Quiz> quizzes = firestore.collection("quiz")
                .whereEqualTo("host_id", userId)
                .get()
                .get()
                .toObjects(Quiz.class);

        return quizzes.stream()
                .map(quiz -> QuizResponseDto.builder()
                        .quiz_id(quiz.getId())
                        .host_id(quiz.getHost_id())
                        .title(quiz.getTitle())
                        .description(quiz.getDescription())
                        .status(quiz.getStatus().name())
                        .categories(quiz.getCategories().stream().map(Category::getName).toList())
                        .start_time(String.valueOf(quiz.getStart_time()))
                        .end_time(String.valueOf(quiz.getEnd_time()))
                        .build())
                .toList();
    }

    // Find quiz by ID
    @SneakyThrows
    public Quiz getQuizById(@NonNull final String quizId) {
        return firestore.collection("quiz").document(quizId).get().get().toObject(Quiz.class);
    }

}
