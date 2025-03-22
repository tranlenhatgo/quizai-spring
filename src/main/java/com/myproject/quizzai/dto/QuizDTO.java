package com.myproject.quizzai.dto;

import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizDTO {
    private static Firestore firestore;

    @Autowired
    public void setFirestore(final Firestore firestore) {
        QuizDTO.firestore = firestore;
        System.out.println("Firestore has been injected: " + (firestore != null));
    }
}
