package com.bigdecimal.clasnapp.report;

import java.sql.Timestamp;
import java.util.UUID;

import com.bigdecimal.clasnapp.report.attendance.Attendance;
import com.bigdecimal.clasnapp.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    private Attendance attendance;

    public Report(User user, String week, String topic, String summary) {
        this.user = user;
        this.week = week;
        this.topic = topic;
        this.summary = summary;
    }
}
