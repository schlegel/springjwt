package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.BaseWebTest;
import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyControllerTest  extends BaseWebTest{

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void testCreateCompany_withRole_Superadmin_wrong_Validation() throws Exception {
        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName(null);
        companyCreate.setDescription("My Descrption");
        companyCreate.getMailPostfixes().add(".de");

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCompany_withRole_Superadmin() throws Exception {
        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("My Company");
        companyCreate.setDescription("My Descrption");
        companyCreate.getMailPostfixes().add(".de");

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCompany_withRole_CompanyAdmin() throws Exception {
        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("My Company");
        companyCreate.setDescription("My Description");
        //companyCreate.getMailPostfixes().add(".de");

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("companyadmin@example.de", "password"))
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
        CompanyCreateDto companyUpdate = new CompanyCreateDto();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("superadmin@example.de", "password"))
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
        CompanyCreateDto companyUpdate = new CompanyCreateDto();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("companyadmin@example.de", "password"))
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