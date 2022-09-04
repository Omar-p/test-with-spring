package com.example.testwithspring.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;
  private LocalDateTime registrationDate;

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }
}