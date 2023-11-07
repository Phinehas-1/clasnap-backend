package com.bigdecimal.clasnapp.domain;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    UUID id;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column(name = "submittedOn", nullable = false)
    private Timestamp submittedOn;

    //TODO This will be a combination of the term name + week name.
    @Column(nullable = false)
    private String week;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String summary;

    @OneToMany
    private List<Attendance> attendances;

    public Report(User user, String week, String topic, String summary) {
        this.user = user;
        this.week = week;
        this.topic = topic;
        this.summary = summary;
    }
}
