package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.BaseAuthContextTest;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyServiceTest extends BaseAuthContextTest {

    @Autowired
    CompanyService companyService;

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
    @WithMockUser(roles = {"SUPER_ADMIN"})
    public void testCreateCompany() throws Exception {
        CompanyCreateDto companyCreate = new CompanyCreateDto();
        companyCreate.setName("My Company");
        companyCreate.setMailPostfixes(new HashSet<>(Arrays.asList(".de")));

        companyService.createCompany(companyCreate);
    }
}