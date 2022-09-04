package com.example.testwithspring.web;

import com.example.testwithspring.domain.RegisterUseCase;
import com.example.testwithspring.domain.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class RegisterMvcController {
  private final RegisterUseCase registerUseCase;

  @PostMapping("/forums/{forumId}/register")
  UserResource register(
      @PathVariable("forumId") Long forumId,
      @Valid @RequestBody UserResource userResource,
      @RequestParam("sendWelcomeMail") boolean sendWelcomeMail) {

    User user = new User(
        userResource.getName(),
        userResource.getEmail());
    Long userId = registerUseCase.registerUser(user, sendWelcomeMail);

    return new UserResource(
        userId,
        user.getName(),
        user.getEmail());
  }

}