package com.github.schlegel.springjwt.service.company.transport;


import com.github.schlegel.springjwt.BaseAuthContextTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyUpdateDtoTest extends BaseAuthContextTest{

    @Autowired
    private Validator validator;

    @Test
    @WithMockUser(roles = {"SUPER_ADMIN"})
    public void createValidCompanyUpdateDto() throws Exception {
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("new name");

        Assert.assertEquals(0 , validator.validate(companyUpdate).size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void createValidCompanyUpdateDtoWithUser() throws Exception {
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("new name");
        Set<ConstraintViolation<CompanyUpdateDto>> results = validator.validate(companyUpdate);

        Assert.assertTrue(results.stream().anyMatch(c -> "field access not allowed".equals(c.getMessage())));
    }

    @Test
    @WithMockUser(roles = {"SUPER_ADMIN"})
    public void createCompanyUpdateDtoInvalid() throws Exception {
        CompanyUpdateDto companyUpdate = new CompanyUpdateDto();
        companyUpdate.setName("1");
        Set<ConstraintViolation<CompanyUpdateDto>> results = validator.validate(companyUpdate);

        Assert.assertTrue(results.stream().anyMatch(c -> c.getMessage()!= null && c.getMessage().startsWith("size must be between 3")));
    }

}