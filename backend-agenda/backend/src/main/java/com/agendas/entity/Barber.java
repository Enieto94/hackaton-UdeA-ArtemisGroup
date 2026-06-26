package com.agendas.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Barber {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    private User user;
}
