package com.myproject.quizzai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(QuestionController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Question Controller", description = "Controller for managing questions")
public class QuestionController {
    public static final String ROOT_MAPPING = "question";


}
