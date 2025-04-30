package com.myproject.quizzai.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import com.myproject.quizzai.model.Status;
import com.myproject.quizzai.utils.TimestampDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Schema(title = "Question Creation Request DTO", accessMode = Schema.AccessMode.WRITE_ONLY)
public class QuestionCreationRequestDto {

    @NotBlank(message = "Question ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "ID of the question")
    private String id;

    @NotBlank(message = "Quiz ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "ID of the quiz this question belongs to")
    private String quiz_id;

    @NotBlank(message = "Content cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Content of the question")
    private String content;

    @NotBlank(message = "Answers cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "List of possible answers for the question")
    private List<String> answers;

    @NotBlank(message = "Correct answer cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Correct answer for the question")
    private String correct_answer;

    @Schema(description = "Status of the question")
    private Status status;

    @Schema(description = "Creation timestamp of the question")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp createdAt;

    @Schema(description = "Last updated timestamp of the question")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp updatedAt;
}