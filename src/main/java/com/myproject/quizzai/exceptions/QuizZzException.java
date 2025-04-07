package com.myproject.quizzai.exceptions;

import java.io.Serial;

public class QuizZzException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public QuizZzException() {
        super();
    }

    public QuizZzException(String message) {
        super(message);
    }
}