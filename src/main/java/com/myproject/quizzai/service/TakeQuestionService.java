package com.myproject.quizzai.service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.myproject.quizzai.dto.TakeQuestionSaveRequestDto;
import com.myproject.quizzai.model.TakeQuestion;
import com.myproject.quizzai.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TakeQuestionService {
    private final Firestore firestore;
    private static final Logger logger = LoggerFactory.getLogger(TakeQuestionService.class);

    private final QuestionService questionService;

    // Method to save a list of questions taken by players
    @SneakyThrows
    public Map<String,String> saveTakeQuestions(List<TakeQuestionSaveRequestDto> takeQuestionDtos) {
        Map<String,String> result = new HashMap<>();

        for (TakeQuestionSaveRequestDto takeQuestionDto : takeQuestionDtos) {
            String id = IdUtil.generateId();
            TakeQuestion takeQuestion = TakeQuestion.builder()
                    .id(id)
                    .take_id(takeQuestionDto.getTake_id())
                    .question_id(takeQuestionDto.getQuestion_id())
                    .answer(takeQuestionDto.getAnswer())
                    .check_answer(takeQuestionDto.getCheck_answer())
                    .created_at(Timestamp.now())
                    .updated_at(Timestamp.now())
                    .build();

            // Save the TakeQuestion object to Firestore
            firestore.collection("take_question").document(id).set(takeQuestion).get();
            result.put(id, "Success");
        }

        return result;
    }
}
