package com.myproject.quizzai.service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
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

@Service
@RequiredArgsConstructor
public class QuizService {
    private final Firestore firestore;

    @SneakyThrows
    public String create(@NonNull final QuizCreationRequestDto quizCreationRequest) {
        String quizId = IdUtil.generateId();

        List<Category> categories = quizCreationRequest.getCategories();

        Quiz quiz = Quiz.builder()
                .id(quizId)
                .host_id(quizCreationRequest.getHost_id())
                .title(quizCreationRequest.getTitle())
                .description(quizCreationRequest.getDescription())
                .categories(categories)
                .status(Status.ACTIVE)
                .start_time(quizCreationRequest.getStart_time())
                .end_time(quizCreationRequest.getEnd_time())
                .createdAt(Timestamp.now())
                .updatedAt(Timestamp.now())
                .build();

        firestore.collection("quiz").document(quizId).set(quiz).get();
        return quizId;
    }

//    @SneakyThrows
//    public QuizResponseDto getQuizById(@NonNull final String id) {
//        Quiz quiz = firestore.collection("quiz").document(id).get().get().toObject(Quiz.class);
//        if (quiz != null) {
//            return QuizResponseDto.builder().id(quiz.getId())
//                    .host_id(quiz.getHost_id())
//                    .title(quiz.getTitle())
//                    .description(quiz.getDescription())
//                    .status(quiz.getStatus())
//                    .categories_id(quiz.getCategories_id())
//                    .code(quiz.getCode())
//                    .start_time(quiz.getStart_time())
//                    .end_time(quiz.getEnd_time())
//                    .createdAt(quiz.getCreatedAt())
//                    .updatedAt(quiz.getUpdatedAt())
//                    .build();
//        } else {
//            return null;
//        }
//    }

}
