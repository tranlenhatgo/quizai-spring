package com.myproject.quizzai.dto;

import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuizDTO {
    private static Firestore firestore;

    @Autowired
    public void setFirestore(final Firestore firestore) {
        QuizDTO.firestore = firestore;
        System.out.println("Firestore has been injected: " + (firestore != null));
    }

    public String createQuiz() {
        String quizId = UUID.randomUUID().toString();
        Quiz quiz = Quiz.builder()
                .id(quizId)
                .host_id(this.hostId)
                .topic_id(this.topicId)
                .title(this.title)
                .description(this.description)
                .status(this.status)
                .categories_id(this.categoriesId)
                .code(this.code)
                .start_time(this.startTime)
                .end_time(this.endTime)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

        firestore.collection("quizzes").document(quizId).set(quiz);
        return quizId;
    }
}
