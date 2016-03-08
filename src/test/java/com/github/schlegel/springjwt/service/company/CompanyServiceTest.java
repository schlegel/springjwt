package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.BaseAuthContextTest;
import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.mockdata.MockDataCreator;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyOutputDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyServiceTest extends BaseAuthContextTest {

    @Autowired
    private MockDataCreator mockDataCreator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void notAllowedWithoutCredentials() {
        companyService.createCompany(null);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = {"USER"})
    public void notAllowedWithNormalUser() {
        companyService.createCompany(null);
    }

    @Test
    @WithMockUser(username = "superadmin@example.de", roles = {"SUPER_ADMIN"})
    public void testCreateCompany() throws Exception {
        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("My Company");
        companyCreate.setMailPostfixes(new HashSet<>(Arrays.asList(".de")));

        CompanyOutputDto result = companyService.createCompany(companyCreate);
        Assert.assertEquals("My Company", result.getName());
    }

    @Test
    @WithMockUser(username = "companyadmin@example.de", roles = {"COMPANY_ADMIN"})
    public void updateCompanyWithCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();
        // Add user to company
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

        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setDescription("Updated Company Description");

        CompanyOutputDto result = companyService.updateCompany(company.getId(), companyUpdate);

        Assert.assertEquals("Updated Company Description", result.getDescription());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "companyadmin@example.de", roles = {"COMPANY_ADMIN"})
    public void updateWrongCompanyWithCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();

        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setDescription("Updated Company Description");

        companyService.updateCompany(UUID.randomUUID(), companyUpdate);
    }

    @Test(expected = ConstraintViolationException.class)
    @WithMockUser(username = "companyadmin@example.de", roles = {"COMPANY_ADMIN"})
    public void updateInvalidCompanyWithCompanyAdmin() throws Exception {
        mockDataCreator.createUsers();
        // Add user to company
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

        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("ACME Industries");

        companyService.updateCompany(company.getId(), companyUpdate);
    }
}