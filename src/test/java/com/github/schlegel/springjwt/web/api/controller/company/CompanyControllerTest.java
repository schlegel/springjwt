package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.Application;
import com.github.schlegel.springjwt.BaseTest;
import com.github.schlegel.springjwt.service.company.CompanyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CompanyControllerTest  extends BaseTest{

    @Test
    public void testCreateCompany_withRole_Superadmin() throws Exception {
        CompanyDTO companyCreate = new CompanyDTO();
        companyCreate.setName("My Company");
        companyCreate.setDescription("My Descrption");
        companyCreate.getMailPostfixes().add(".de");

        mockMvc.perform(post("/companies").header(headerToken, authenticateUser("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCompany_withRole_CompanyAdmin() throws Exception {
        CompanyDTO companyCreate = new CompanyDTO();
        companyCreate.setName(null);
        companyCreate.setDescription("My Descrption");
        //companyCreate.getMailPostfixes().add(".de");

        mockMvc.perform(post("/companies").header(headerToken, authenticateUser("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andExpect(status().isForbidden());
    }

}