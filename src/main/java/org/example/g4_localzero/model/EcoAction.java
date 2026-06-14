package org.example.g4_localzero.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eco_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EcoAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType;

    private double amount;

    private double co2Saved;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
