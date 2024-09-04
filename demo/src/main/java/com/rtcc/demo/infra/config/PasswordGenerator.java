package com.rtcc.demo.infra.config;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()-_=+[]{}";
    private static final int PASSWORD_LENGTH = 12; //  Ajustar o comprimento da senha conforme necessário

    private final SecureRandom random = new SecureRandom(); //TODO: usar o @Autowired para injetar a dependência. Próxima iteracao

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

}
