package com.myproject.quizzai.dto;

import com.google.cloud.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "Take Question Save Request DTO", accessMode = Schema.AccessMode.WRITE_ONLY)
public class TakeQuestionSaveRequestDto {

    @NotBlank(message = "Take ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "ID of the take this question belongs to")
    private String take_id;

    @NotBlank(message = "Question ID cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "ID of the question this answer belongs to")
    private String question_id;

    @NotBlank(message = "Answer cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Answer provided by the player")
    private String answer;

    @NotBlank(message = "Check Answer cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Check answer provided by the player")
    private String check_answer;
}
