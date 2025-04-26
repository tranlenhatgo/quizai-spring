package com.myproject.quizzai.controller.utils;

import java.util.UUID;

public class IdUtil {
    public static String generateId(){
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
