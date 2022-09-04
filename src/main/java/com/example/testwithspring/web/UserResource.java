package com.example.testwithspring.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class UserResource {

  private final long id;
  @NotNull
  private final String name;

  @NotNull
  private final String email;

  private LocalDateTime registrationDate;

  public UserResource(
      long id, @JsonProperty("name") String name,
      @JsonProperty("email") String email) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.registrationDate = null;
  }
}