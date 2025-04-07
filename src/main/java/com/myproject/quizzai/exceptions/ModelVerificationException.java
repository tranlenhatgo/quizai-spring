package com.myproject.quizzai.exceptions;

import java.io.Serial;

public class ModelVerificationException extends QuizZzException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ModelVerificationException() {
        super();
    }

    public ModelVerificationException(String message) {
        super(message);
    }
}
