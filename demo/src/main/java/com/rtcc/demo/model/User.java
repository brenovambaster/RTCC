package com.rtcc.demo.model;

import com.rtcc.demo.infra.config.UserRole;
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

    public User(String name, String email, String encode) {
        this.name = name;
        this.email = email;
        this.password = encode;
        this.role = UserRole.COORDINATOR;
    }
}
