package com.myproject.quizzai.model;


import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @DocumentId
    private String id;
    private String quiz_id;
    private String category_id;
    private String content;
    private List<String> answers;
    private Integer correct_answer;
    private String score;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
