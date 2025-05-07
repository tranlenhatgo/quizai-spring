package com.myproject.quizzai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TakeQuestionController.ROOT_MAPPING)
@RequiredArgsConstructor
@Tag(name = "Take Question Controller", description = "Controller for managing questions taked by players")
public class TakeQuestionController {
    public static final String ROOT_MAPPING = "take-question";

}
