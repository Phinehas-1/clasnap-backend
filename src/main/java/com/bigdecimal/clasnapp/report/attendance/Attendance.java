package com.bigdecimal.clasnapp.report.attendance;

import com.bigdecimal.clasnapp.report.student.Student;
import com.bigdecimal.clasnapp.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  /* This will be a combination of the term name + week name */
  @Column(nullable = false)
  private String week;

  @ManyToMany
  private List<Student> students = new ArrayList<Student>();

  public Attendance(String week) {
    this.week = week;
  }
}
