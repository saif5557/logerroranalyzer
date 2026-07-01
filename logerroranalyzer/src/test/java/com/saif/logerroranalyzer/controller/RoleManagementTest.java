package com.saif.logerroranalyzer.controller;

import com.saif.logerroranalyzer.entity.User;
import com.saif.logerroranalyzer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleManagementTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Ensure we have a clean state or specific users for testing if using Real DB
        // But here we are using H2 in memory usually, so it resets or persists per
        // context.
        // We will mock users via @WithMockUser for Controller tests,
        // but for registration flow we check DB side effects if possible or just
        // MockMvc response.
    }

    @Test
    public void testRegistrationCreatesUserRole() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "testuser_rbac")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection());

        User user = userRepository.findByUsername("testuser_rbac").orElseThrow();
        assert (user.getRole().equals("ROLE_USER"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    public void testUserCannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testAdminCanAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    public void testUserCannotCreateErrorCode() throws Exception {
        mockMvc.perform(post("/api/error-codes")
                .contentType("application/json")
                .content("{\"errorCode\":\"FAIL001\"}"))
                .andExpect(status().isForbidden());
    }
}
