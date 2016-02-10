package com.github.schlegel.springjwt.web.api.controller.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.schlegel.springjwt.BaseWebTest;
import com.github.schlegel.springjwt.mockdata.MockDataCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthControllerTest extends BaseWebTest{

    @Autowired
    private MockDataCreator mockDataCreator;

    @Test
    public void testWrongAuthentication() throws Exception {
        // only password
        mockMvc.perform(post("/authentication").param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized());

        // only username
        mockMvc.perform(post("/authentication").param("email", "wronguser"))
                .andExpect(status().isUnauthorized());

        // wrong username and password
        mockMvc.perform(post("/authentication").param("email", "wronguser").param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthentication() throws Exception {
        mockDataCreator.createUsers();

        // login user
        mockMvc.perform(post("/authentication").param("email", "user@example.de").param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAuthSecuredPing() throws Exception {
        mockDataCreator.createUsers();

        // negative authentication
        mockMvc.perform(get("/securedping"))
                .andExpect(status().isUnauthorized());


        // with wrong jwt token
        mockMvc.perform(get("/securedping").header(headerToken,"wrongtoken"))
                .andExpect(status().isUnauthorized());

        // login user
        String jwtToken = authenticateUser("user@example.de", "password");

        // positive authentication
        mockMvc.perform(get("/securedping").header(headerToken,jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Secured World"));
    }

    private String authenticateUser(String email, String password) throws Exception {
        String result = mockMvc.perform(post("/authentication").param("email", email).param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonResult = new ObjectMapper().readTree(result);
        return jsonResult.get("token").textValue();
    }
}
