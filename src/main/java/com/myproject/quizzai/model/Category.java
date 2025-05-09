package com.myproject.quizzai.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Documented;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @DocumentId
    private String id;
    private String name;
    private String description;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
