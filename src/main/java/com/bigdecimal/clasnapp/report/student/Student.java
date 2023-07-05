package com.bigdecimal.clasnapp.report.student;

import java.util.UUID;

import com.bigdecimal.clasnapp.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String name;

    private Boolean laptop;

    private Long score;

    public Student(String name, Boolean laptop) {
        this.name = name;
        this.laptop = laptop;
    }
}
