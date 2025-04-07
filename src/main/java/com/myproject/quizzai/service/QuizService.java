package com.myproject.quizzai.service;

import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.QuizCreationRequestDto;
import com.myproject.quizzai.model.Quiz;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final Firestore firestore;



    @SneakyThrows
    public String create(@NonNull final QuizCreationRequestDto quizCreationRequest) {
        String quizId = UUID.randomUUID().toString();

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setHost_id(quizCreationRequest.getHost_id());
        quiz.setTopic_id(quizCreationRequest.getTopic_id());
        quiz.setTitle(quizCreationRequest.getTitle());
        quiz.setDescription(quizCreationRequest.getDescription());
        quiz.setCategories_id(quizCreationRequest.getCategories_id());
        quiz.setStart_time(quizCreationRequest.getStart_time());
        quiz.setEnd_time(quizCreationRequest.getEnd_time());
        quiz.setCreatedAt(quizCreationRequest.getCreated_at());
        quiz.setUpdatedAt(quizCreationRequest.getUpdated_at());

        firestore.collection("quizzes").document(quizId).set(quiz).get();
        return quizId;
    }

}
