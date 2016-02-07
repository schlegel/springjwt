package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.BaseWebTest;
import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.mockdata.MockDataCreator;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyControllerTest  extends BaseWebTest{

    @Autowired
    private MockDataCreator mockDataCreator;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    /* -------------------
     CREATE COMPANY AUTH TESTS
    ------------------- */

    @Test
    public void createValidCompanyWithSuperAdmin() throws Exception {
        mockDataCreator.createUsers();

        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("Company Name");
        companyCreate.setDescription("Company Description");
        companyCreate.setMailPostfixes(new HashSet<>(Arrays.asList(".de")));

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createValidCompanyButWithCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("Company Name");
        companyCreate.setDescription("Company Description");
        companyCreate.setMailPostfixes(new HashSet<>(Arrays.asList(".de")));

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createValidCompanyButWithUser() throws Exception {
        mockDataCreator.createUsers();

        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("Company Name");
        companyCreate.setDescription("Company Description");
        companyCreate.setMailPostfixes(new HashSet<>(Arrays.asList(".de")));

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("user@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    /* -------------------
     CREATE COMPANY VALIDATION TESTS
    ------------------- */

    @Test
    public void createInvalidCompanyWithSuperAdmin() throws Exception {
        mockDataCreator.createUsers();

        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setDescription("Only Description");

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createInvalidCompanyWithCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setDescription("Company Description");

        mockMvc.perform(post("/companies").header(headerToken, getAuthToken("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyCreate)))
                .andDo(print())
                .andExpect(status().isForbidden()); // access audit is before validation
    }

    /* -------------------
     EDIT COMPANY AUTH TESTS
    ------------------- */

    @Test
    public void updateCompanyWithSuperAdmin() throws Exception {
        mockDataCreator.createUsers();

        // Create initial company
        Company company = new Company();
        company.setName("Company Name");
        company.setDescription("Company Description");
        companyRepository.save(company);

        // Create Patch Company DTO
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Company Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company Name"))
                .andExpect(jsonPath("$.description").value("Updated Company Description"));
    }

    @Test
    public void updateCompanyWithValidCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        // Add user to compay
        User companyAdmin = userRepository.findByEmail("companyadmin@example.de");

        Set<User> users =  new HashSet<>();
        users.add(companyAdmin);

        // Create initial company
        Company company = new Company();
        company.setName("Company Name");
        company.setDescription("Company Description");
        company.setUsers(users);
        companyRepository.save(company);

        companyAdmin.setCompany(company);
        userRepository.save(companyAdmin);

        // Create Patch Company Dto
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setDescription("Updated Company Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Company Description"))
                .andExpect(jsonPath("$.users").isNotEmpty());
    }

    @Test
    public void updateCompanyWithInvalidCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        // Create initial company
        Company company = new Company();
        company.setName("Company Name");
        company.setDescription("Company Description");
        companyRepository.save(company);

        // Create Patch Company Dto
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("Updated Company Name");
        companyUpdate.setDescription("Updated Company Description");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    /* -------------------
     EDIT COMPANY VALIDATION TESTS
    ------------------- */

    @Test
    public void updateInvalidCompanyWithSuperAdmin() throws Exception {
        mockDataCreator.createUsers();

        // Create initial company
        Company company = new Company();
        company.setName("Company Name");
        companyRepository.save(company);

        // Create Patch Company DTO
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("1"); // company name to short

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("superadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isNotEmpty());
    }

    @Test
    public void updateInvalidCompanyWithValidCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        // Create initial company
        Company company = new Company();
        company.setName("Company Name");
        companyRepository.save(company);

        User companyAdmin = userRepository.findByEmail("companyadmin@example.de");
        companyAdmin.setCompany(company);
        userRepository.save(companyAdmin);

        // Create Patch Company DTO
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("Updated Company Name");

        mockMvc.perform(patch("/companies/" + company.getId()).header(headerToken, getAuthToken("companyadmin@example.de", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyUpdate)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isNotEmpty());
    }
}