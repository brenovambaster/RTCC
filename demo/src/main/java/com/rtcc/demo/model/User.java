package com.rtcc.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    @Column(name = "email_verified")
    private boolean emailVerified = false;  // Campo para verificar o email

    @Column(name = "verification_token")
    private String verificationToken;  // Token temporário para verificação

    public User(String name, String email, String encode, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = encode;
        this.role = role;
    }
}
