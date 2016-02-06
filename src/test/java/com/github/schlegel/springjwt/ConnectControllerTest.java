package com.github.schlegel.springjwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ConnectControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.token.header}")
    protected String headerToken;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .defaultRequest(get("/"))
                .build();

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

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
        // login user
        String result = mockMvc.perform(post("/authentication").param("email", "user@example.de").param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAuthSecuredPing() throws Exception {
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
