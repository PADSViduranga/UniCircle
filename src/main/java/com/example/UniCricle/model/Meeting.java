package com.example.UniCricle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String description;

    private LocalDateTime date = LocalDateTime.now();
}