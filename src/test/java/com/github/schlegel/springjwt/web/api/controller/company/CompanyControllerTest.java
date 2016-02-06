package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.Application;
import com.github.schlegel.springjwt.BaseTest;
import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.service.company.CompanyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CompanyControllerTest  extends BaseTest{

    @Autowired
    CompanyRepository companyRepository;

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

    @Test
    public void testEditCompany_withRole_Superadmin() throws Exception {
        // Create initial company
        Company company = new Company();
        company.setName("MyCompany");
        company.setDescription("MyDescription");
        companyRepository.save(company);

        // Create Patch Company DTO
        CompanyDTO companyUpdate = new CompanyDTO();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, authenticateUser("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testEditCompany_withCompanyAdmin_ownCompany() throws Exception {
        // Create initial company
        Company company = new Company();
        company.setName("MyCompany");
        company.setDescription("MyDescription");
        companyRepository.save(company);

        // Create Patch Company DTO
        CompanyDTO companyUpdate = new CompanyDTO();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, authenticateUser("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testEditCompany_withCompanyAdmin_otherCompany() throws Exception {

    }
}