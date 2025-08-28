package com.audiobea.crm.app.controller.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.audiobea.crm.app.constant.TestConstants.LOGIN_PATH;
import static com.audiobea.crm.app.constant.TestConstants.LOGIN_PATH_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Standalone setup avoids loading the full Spring context
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthController())
                        .addPlaceholderValue(LOGIN_PATH, LOGIN_PATH_VALUE)
                        .build();
    }

    @Test
    @DisplayName("POST /login returns 200 OK when JSON payload is provided")
    void login_ReturnsOk() throws Exception {
        String jsonBody = "{\n" +
                "  \"username\": \"user@example.com\",\n" +
                "  \"password\": \"secret\",\n" +
                "  \"roles\": []\n" +
                "}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }
}
