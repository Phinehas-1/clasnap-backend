package com.bigdecimal.clasnapp.domain.attendance;

import java.util.UUID;

import com.bigdecimal.clasnapp.domain.calendar.Calendar;
import com.bigdecimal.clasnapp.domain.student.Student;
import com.bigdecimal.clasnapp.domain.user.data.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Attendance {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  /* This will be a combination of the term name + week name */
  // @Column(nullable = false)
  // private String week;

  @ManyToOne
  private Calendar calendar;

  private Boolean laptop;

  private Long score;

  @ManyToOne
  private Student student;

  public Attendance(Calendar calendar) {
    this.calendar = calendar;
  }
}
