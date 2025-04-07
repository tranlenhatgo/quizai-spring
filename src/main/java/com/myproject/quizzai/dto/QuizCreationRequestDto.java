package com.myproject.quizzai.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import com.myproject.quizzai.controller.utils.TimestampDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "Quiz Creation Request DTO", accessMode = Schema.AccessMode.WRITE_ONLY)
public class QuizCreationRequestDto {
    @NotBlank(message = "Host ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String host_id;

    @NotBlank(message = "Topic ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String topic_id;

    @NotBlank(message = "Title cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    private String description;

    @Schema(description = "List of category IDs related to the quiz")
    private List<String> categories_id;

    @Schema(description = "Start time of the quiz")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp start_time;

    @Schema(description = "End time of the quiz")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp end_time;

    @Schema(description = "Creation timestamp of the quiz")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp created_at;

    @Schema(description = "Last updated timestamp of the quiz")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp updated_at;
}

