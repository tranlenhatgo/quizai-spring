package com.myproject.quizzai.dto;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
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

    public String createQuizToFirebase(Quiz quiz) {
        String quizId = UUID.randomUUID().toString();
        quiz.setId(quizId);
        firestore.collection("quizzes").document(quizId).set(quiz);
        return quizId;
    }

    public Quiz getQuizFromFirebaseById(String quizId) {
        try {
            ApiFuture<DocumentSnapshot> future = firestore.collection("quizzes").document(quizId).get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Quiz.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
