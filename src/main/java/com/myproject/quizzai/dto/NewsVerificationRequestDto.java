package com.myproject.quizzai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "News Verification Request DTO", accessMode = Schema.AccessMode.WRITE_ONLY)
public class NewsVerificationRequestDto {

    @NotBlank(message = "Content cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "The news content to be verified")
    private String content;

    @Schema(description = "User ID making the request")
    private String userId;
}