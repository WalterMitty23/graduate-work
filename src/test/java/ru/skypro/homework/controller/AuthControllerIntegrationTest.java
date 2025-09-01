package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.skypro.homework.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void login_withValidUser_shouldReturn200() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@test.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_withInvalidPassword_shouldReturn401() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@test.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_newUser_shouldReturn201() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"new@test.com\",\"password\":\"pass123\",\"firstName\":\"F\",\"lastName\":\"L\",\"phone\":\"123456\",\"role\":\"USER\"}"))
                .andExpect(status().isCreated());
    }
}
