package com.myproject.quizzai.service;

import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.QuestionCreationRequestDto;
import com.myproject.quizzai.model.Question;
import com.myproject.quizzai.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final Firestore firestore;

    @SneakyThrows
    public Map<String, String> createQuestions(List<QuestionCreationRequestDto> questionDtos) {
        Map<String, String> result = new HashMap<>();

        for (QuestionCreationRequestDto questionDto : questionDtos) {
            try {
                String questionId = IdUtil.generateId();
                Question question = Question.builder()
                        .id(questionId)
                        .quiz_id(questionDto.getQuiz_id())
                        .content(questionDto.getContent())
                        .answers(questionDto.getAnswers())
                        .correct_answer(questionDto.getCorrect_answer())
                        .status(questionDto.getStatus())
                        .createdAt(questionDto.getCreatedAt())
                        .updatedAt(questionDto.getUpdatedAt())
                        .build();

                firestore.collection("questions").document(questionId).set(question).get();
                result.put(questionId, "Success");
            } catch (Exception e) {
                result.put("unknown", "Fail: " + e.getMessage());
            }
        }

        return result;
    }
}
