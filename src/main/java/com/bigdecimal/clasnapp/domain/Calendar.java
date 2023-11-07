package com.bigdecimal.clasnapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Calendar {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String week;

  @Column(unique = true, nullable = false)
  private String weekName;

  @CreationTimestamp
  private Timestamp createdAt;

  @JsonIgnore
  @ManyToOne
  private User user;

  public Calendar(String week, String weekName) {
    this.weekName = weekName;
    this.week = week;
  }
}
