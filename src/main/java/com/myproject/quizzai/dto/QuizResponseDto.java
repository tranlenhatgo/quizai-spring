package com.myproject.quizzai.dto;

import com.google.cloud.Timestamp;
import com.myproject.quizzai.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(title = "QuizResponse", accessMode = Schema.AccessMode.READ_ONLY)
public class QuizResponseDto {
    private String id;
    private String host_id;
    private String title;
    private String description;
    private Status status;
    private List<String> categories_id;
    private String code;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
