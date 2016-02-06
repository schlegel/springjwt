package com.github.schlegel.springjwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.security.jwt.service.AuthenticationServiceUsernamePassword;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BaseWebTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private AuthenticationServiceUsernamePassword authenticationServiceUsernamePassword;

    @Value("${jwt.token.header}")
    protected String headerToken;

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .defaultRequest(get("/"))
                .build();

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected String getAuthToken(String email, String password) throws Exception {
        return authenticationServiceUsernamePassword.authenticate(email, password).serialize();
    }
}
