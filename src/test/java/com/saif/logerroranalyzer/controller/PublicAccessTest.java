package com.saif.logerroranalyzer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PublicAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePageIsPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReportsPageIsPublic() throws Exception {
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCssIsPublic() throws Exception {
        // Assuming there might be a CSS file, or just checking endpoint access config
        // If file doesn't exist it might be 404, but not 302/401.
        // We generally check 200 or 404, NOT 302.
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk());
    }
}
