package com.example.testwithspring.web;

import com.example.testwithspring.domain.RegisterUseCase;
import com.example.testwithspring.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegisterMvcController.class)
class ControllerExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RegisterUseCase registerUseCase;

  @Test
  void whenValidInput_thenReturns200() throws Exception {

    UserResource userResource = new UserResource(0, "lionel", "messi");

    mockMvc.perform(post("/forums/{forumId}/register", 42)
            .contentType("application/json")
            .param("sendWelcomeMail", "true")
            .content(objectMapper.writeValueAsString(userResource)))
        .andExpect(status().isOk());
  }


  @Test
  void whenNullValue_thenReturns400() throws Exception {
    UserResource user = new UserResource(0, null, "zaphod@galaxy.net");

    mockMvc.perform(post("/forums/{forumId}/register", 42)
            .contentType("application/json")
            .param("sendWelcomeMail", "true")
            .content(objectMapper.writeValueAsString(user)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void whenValidInput_thenMapsToBusinessModel() throws Exception {
    UserResource user = new UserResource(0, "Zaphod", "zaphod@galaxy.net");
    mockMvc.perform(post("/forums/{forumId}/register", 42)
            .contentType("application/json")
            .param("sendWelcomeMail", "true")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk());

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(registerUseCase, times(1)).registerUser(userCaptor.capture(), eq(true));
    assertThat(userCaptor.getValue().getName()).isEqualTo("Zaphod");
    assertThat(userCaptor.getValue().getEmail()).isEqualTo("zaphod@galaxy.net");
  }

  @Test
  void whenValidInput_thenReturnsUserResource() throws Exception {
    UserResource user = new UserResource(0, "Zaphod", "zaphod@galaxy.net");

    MvcResult mvcResult = mockMvc.perform(post("/forums/{forumId}/register", 42)
            .contentType("application/json")
            .param("sendWelcomeMail", "true")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andReturn();

    UserResource expectedResponseBody = user;
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
        objectMapper.writeValueAsString(expectedResponseBody));
  }

  @Test
  void whenNullValue_thenReturns400AndErrorResult() throws Exception {
    UserResource user = new UserResource(0,null, "zaphod@galaxy.net");

    MvcResult mvcResult = mockMvc.perform(post("/forums/{forumId}/register", 42)
          .contentType("application/json")
        .param("sendWelcomeMail", "true")
        .content(objectMapper.writeValueAsString(user)))
          .andExpect(status().isBadRequest())
        .andReturn();

    ErrorResult expectedErrorResponse = new ErrorResult("name", "must not be null");
    String actualResponseBody =
        mvcResult.getResponse().getContentAsString();
    String expectedResponseBody =
        objectMapper.writeValueAsString(expectedErrorResponse);
    assertThat(actualResponseBody)
        .isEqualToIgnoringWhitespace(expectedResponseBody);
  }
}