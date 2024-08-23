package com.rtcc.demo.util;

import java.util.UUID;

public class TokenGenerator {
    public static String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
