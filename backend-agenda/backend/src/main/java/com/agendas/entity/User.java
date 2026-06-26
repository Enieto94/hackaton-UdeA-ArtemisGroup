package com.agendas.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue
    private Long id;

    private String nombre;
    private String telefono;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}