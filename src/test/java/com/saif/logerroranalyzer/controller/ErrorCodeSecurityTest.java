package com.saif.logerroranalyzer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.saif.logerroranalyzer.service.ErrorCodeService;
import com.saif.logerroranalyzer.dto.ErrorCodeDto;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorCodeSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorCodeService errorCodeService;

    @Test
    public void testGetErrorCodes_Unauthorized() throws Exception {
        // Now expect Redirection to Login, because even GET is protected
        mockMvc.perform(get("/api/error-codes"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void testGetErrorCodes_Authorized() throws Exception {
        Mockito.when(errorCodeService.getAllActiveErrorCodes()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/error-codes"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateErrorCode_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/error-codes") // Without mock user, we are anonymous
                .contentType("application/json")
                .content("{\"errorCode\":\"E001\"}"))
                .andExpect(status().is3xxRedirection()); // Redirects to login
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testCreateErrorCode_Authorized() throws Exception {
        ErrorCodeDto dto = new ErrorCodeDto();
        dto.setErrorCode("SEC001");
        Mockito.when(errorCodeService.createErrorCode(ArgumentMatchers.any())).thenReturn(dto);

        mockMvc.perform(post("/api/error-codes")
                .contentType("application/json")
                .content(
                        "{\"errorCode\":\"SEC001\",\"description\":\"Security Test\",\"applicationType\":\"MES\",\"errorType\":\"SYSTEM_ERROR\",\"errorSeverity\":\"CRITICAL\"}"))
                .andExpect(status().isCreated());
    }
}
