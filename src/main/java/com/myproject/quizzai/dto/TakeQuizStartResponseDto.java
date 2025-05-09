package com.myproject.quizzai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(title = "Take Quiz Start Response Dto", accessMode = Schema.AccessMode.READ_ONLY)
public class TakeQuizStartResponseDto {
    private String takeId;
    private List<QuestionResponseDto> questionResponseDtos;
}
