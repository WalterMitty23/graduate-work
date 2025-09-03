package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skypro.homework.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getAllAds_shouldReturn200_forUser() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllAds_shouldReturn200_forAdmin() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAds_shouldReturn401_withoutAuth() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized());
    }
}
