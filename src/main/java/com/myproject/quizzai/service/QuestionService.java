package com.myproject.quizzai.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.myproject.quizzai.dto.QuestionCreationRequestDto;
import com.myproject.quizzai.model.Question;
import com.myproject.quizzai.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final Firestore firestore;

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);


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

    public List<Question> getQuestionsByQuizId(String quizId) {
        List<Question> questions = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("questions")
                    .whereEqualTo("quiz_id", quizId)
                    .get();
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Question question = document.toObject(Question.class);
                questions.add(question);
            }
        } catch (Exception e) {
            logger.error("Error fetching questions for quizId {}: {}", quizId, e.getMessage());
        }
        return questions;
    }

    public Question getQuestionById(String id) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("questions")
                    .whereEqualTo("id", id)
                    .get();
            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                return document.toObject(Question.class);
            }
        } catch (Exception e) {
            logger.error("Error fetching question by ID {}: {}", id, e.getMessage());
        }
        return null;
    }
}
